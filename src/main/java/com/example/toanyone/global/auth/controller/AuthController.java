package com.example.toanyone.global.auth.controller;

import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import com.example.toanyone.global.auth.service.AuthService;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<ApiResponse<String>>  signup(@Valid @RequestBody AuthRequestDto.Signup signupRequest) {
        return ApiResponse.onSuccess(SuccessStatus.CREATED, authService.signup(signupRequest));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<AuthResponseDto.CreateToken>> signin(@Valid @RequestBody AuthRequestDto.Signin signin) {
        return ApiResponse.onSuccess(SuccessStatus.OK, authService.signin(signin));
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<ApiResponse<AuthResponseDto.CreateToken>> reissue(HttpServletRequest request) {
        return ApiResponse.onSuccess(SuccessStatus.OK, authService.reissue(request));
    }

}