package com.example.toanyone.global.auth.controller;

import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto.CreateToken;
import com.example.toanyone.global.auth.service.AuthService;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<AuthResponseDto.CreateToken>>  signup(@Valid @RequestBody AuthRequestDto.Signup signupRequest) {
        return ApiResponse.onSuccess(SuccessStatus._OK, authService.signup(signupRequest));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<AuthResponseDto.CreateToken>> signin(@Valid @RequestBody AuthRequestDto.Signin signin) {
        return ApiResponse.onSuccess(SuccessStatus._OK, authService.signin(signin));
    }
}
