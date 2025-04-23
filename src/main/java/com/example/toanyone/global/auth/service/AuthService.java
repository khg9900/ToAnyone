package com.example.toanyone.global.auth.service;

import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;

public interface AuthService {

    AuthResponseDto.CreateToken signup(AuthRequestDto.Signup signupRequest);

    AuthResponseDto.CreateToken signin(AuthRequestDto.Signin signinRequest);
}
