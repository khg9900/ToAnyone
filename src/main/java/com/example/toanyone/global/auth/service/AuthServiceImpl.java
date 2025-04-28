package com.example.toanyone.global.auth.service;


import static com.example.toanyone.global.common.code.ErrorStatus.EMAIL_ALREADY_EXISTS;
import static com.example.toanyone.global.common.code.ErrorStatus.INVALID_JWT_TOKEN;
import static com.example.toanyone.global.common.code.ErrorStatus.NICKNAME_ALREADY_EXISTS;
import static com.example.toanyone.global.common.code.ErrorStatus.PHONE_ALREADY_EXISTS;
import static com.example.toanyone.global.common.code.ErrorStatus.USER_ALREADY_DELETED;
import static com.example.toanyone.global.common.code.ErrorStatus.USER_NOT_FOUND;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthRequestDto.Signup;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import com.example.toanyone.global.auth.entity.Refresh;
import com.example.toanyone.global.auth.jwt.JwtUtil;
import com.example.toanyone.global.auth.repository.RefreshRepository;
import com.example.toanyone.global.common.error.ApiException;
import com.example.toanyone.global.config.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public void signup(Signup signupRequest) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ApiException(EMAIL_ALREADY_EXISTS);
        }
        // 닉네임 중복 체크
        if (userRepository.existsByNickname(signupRequest.getNickname())) {
            throw new ApiException(NICKNAME_ALREADY_EXISTS);
        }
        // 전화번호 중복 체크
        if (userRepository.existsByPhone(signupRequest.getPhone())) {
            throw new ApiException(PHONE_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // User 객체 생성
        User user = new User(
            signupRequest.getEmail(),
            encodedPassword,
            signupRequest.getUsername(),
            UserRole.of(signupRequest.getRole()),
            signupRequest.getNickname(),
            signupRequest.getPhone(),
            signupRequest.getAddress(),
            signupRequest.getGender(),
            signupRequest.getBirth()
        );

        userRepository.save(user);
    }

    @Transactional
    @Override
    public AuthResponseDto.CreateToken login(AuthRequestDto.Login signinRequest) {

        // 가입여부 확인
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
            () -> new ApiException(USER_NOT_FOUND));

        // 탈퇴한 회원인지 확인
        if (user.isDeleted()) {
            throw new ApiException(USER_ALREADY_DELETED);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        // 로그인 성공 시 토큰 발급
        String access = jwtUtil.createToken("access", user.getId(), user.getEmail(), user.getUserRole());
        String refresh = jwtUtil.createToken("refresh", user.getId(), user.getEmail(), user.getUserRole());

        // refreshToken DB에 저장
        jwtUtil.saveRefreshToken(user.getId(), refresh);

        return new AuthResponseDto.CreateToken(access, refresh);
    }

    @Transactional
    @Override
    public AuthResponseDto.CreateToken reissue(Long userId, HttpServletRequest request) {

        // 헤더에서 토큰 가져오기 (검증은 Filter 에서 완료)
        String jwt = request.getHeader("Authorization");

        // 로그인 유저 정보로 DB에 저장된 Refresh 토큰을 찾기
        Refresh refresh = refreshRepository.findByUserId(userId);
        String existingToken = refresh.getRefreshToken();

        // 일치 여부 확인
        if (!jwt.equals(existingToken)) {
            throw new ApiException(INVALID_JWT_TOKEN);
        }

        // 유저 정보 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        // 토큰 재발급
        String newAccess = jwtUtil.createToken("access", userId, user.getEmail(), user.getUserRole());
        String newRefresh = jwtUtil.createToken("refresh", userId, user.getEmail(), user.getUserRole());

        // DB에 저장된 기존 refresh token 삭제 후 저장
        refreshRepository.deleteByUserId(userId);
        jwtUtil.saveRefreshToken(userId, newRefresh);

        return new AuthResponseDto.CreateToken(newAccess, newRefresh);
    }

    public void logout(Long userId) {

        // 로그인 정보로 DB 에서 refresh Token 찾기
        Refresh refresh = refreshRepository.findByUserId(userId);

        // 토큰 삭제
        refreshRepository.deleteByUserId(userId);
    }

}
