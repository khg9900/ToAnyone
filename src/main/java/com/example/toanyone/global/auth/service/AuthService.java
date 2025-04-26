package com.example.toanyone.global.auth.service;

import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    String signup(AuthRequestDto.Signup signupRequest);

    AuthResponseDto.CreateToken login(AuthRequestDto.Login signinRequest);

    AuthResponseDto.CreateToken reissue(HttpServletRequest request);

    String logout(Long userId);
}
