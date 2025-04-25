package com.example.toanyone.global.auth.service;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import com.example.toanyone.global.auth.jwt.JwtUtil;
import com.example.toanyone.global.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public AuthResponseDto.CreateToken signup(AuthRequestDto.Signup signupRequest) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = new User(
            signupRequest.getEmail(),
            encodedPassword,
            signupRequest.getUsername(),
            signupRequest.getRole(),
            signupRequest.getNickname(),
            signupRequest.getPhone(),
            signupRequest.getAddress(),
            signupRequest.getGender(),
            signupRequest.getBirth()
        );

        User savedUser = userRepository.save(newUser);

//        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());

        return new AuthResponseDto.CreateToken("회원가입 성공");
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponseDto.CreateToken signin(AuthRequestDto.Signin signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
            () -> new RuntimeException("가입되지 않은 유저입니다."));

        // 탈퇴한 회원인지 확인합니다.
        if (user.isDeleted()) {
            throw new RuntimeException("탈퇴한 회원입니다.");
        }

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new AuthResponseDto.CreateToken(bearerToken);
    }

}
