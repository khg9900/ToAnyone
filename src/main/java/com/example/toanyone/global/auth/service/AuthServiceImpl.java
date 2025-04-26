package com.example.toanyone.global.auth.service;


import static com.example.toanyone.global.common.code.ErrorStatus.EXPIRED_JWT_TOKEN;
import static com.example.toanyone.global.common.code.ErrorStatus.TOKEN_INVALID;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthRequestDto.Signup;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
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
    public String signup(Signup signupRequest) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

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

        return "회원가입 성공";
    }

    @Transactional
    @Override
    public AuthResponseDto.CreateToken signin(AuthRequestDto.Signin signinRequest) {

        // 가입여부 확인
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
            () -> new RuntimeException("가입되지 않은 유저입니다."));

        // 탈퇴한 회원인지 확인
        if (user.isDeleted()) {
            throw new RuntimeException("탈퇴한 회원입니다.");
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        // 로그인 성공 시 토큰 발급
        String access = jwtUtil.createToken("access", user.getId(), user.getEmail(), user.getUserRole());
        String refresh = jwtUtil.createToken("refresh", user.getId(), user.getEmail(), user.getUserRole());

        // refreshToken 저장
        jwtUtil.saveRefreshToken(user.getId(), refresh);

        return new AuthResponseDto.CreateToken(access, refresh);
    }

    @Transactional
    @Override
    public AuthResponseDto.CreateToken reissue(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization");
        String token = jwtUtil.substringToken(jwt);

        // 토큰 만료 여부 확인
        if (jwtUtil.isExpired(token)) {
            throw new ApiException(EXPIRED_JWT_TOKEN);
        }

        // 토큰 타입 & DB 저장 여부 확인
        if (!jwtUtil.getTokenCategory(token).equals("refresh") || !jwtUtil.isExpired(token)) {
            throw new ApiException(TOKEN_INVALID);
        }

        // 유저 정보 조회
        long userId = Long.parseLong(jwtUtil.extractClaims(token).getSubject());
        String email = jwtUtil.extractClaims(token).get("email", String.class);
        UserRole userRole = UserRole.of(jwtUtil.extractClaims(token).get("userRole", String.class));

        // 토큰 재발급
        String newAccess = jwtUtil.createToken("access", userId, email, userRole);
        String newRefresh = jwtUtil.createToken("refresh", userId, email, userRole);

        // DB에 저장된 기존 refresh token 삭제 후 저장
        refreshRepository.deleteByRefresh(jwt);
        jwtUtil.saveRefreshToken(userId, newRefresh);

        return new AuthResponseDto.CreateToken(newAccess, newRefresh);
    }

}
