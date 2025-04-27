package com.example.toanyone.global.auth.controller;

import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import com.example.toanyone.global.auth.dto.AuthUser;
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
    public ResponseEntity<ApiResponse<String>>  signup(@RequestBody @Valid AuthRequestDto.Signup signupRequest) {
        return ApiResponse.onSuccess(SuccessStatus.CREATED, authService.signup(signupRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponseDto.CreateToken>> login(@Valid @RequestBody AuthRequestDto.Login login) {
        return ApiResponse.onSuccess(SuccessStatus.OK, authService.login(login));
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<ApiResponse<AuthResponseDto.CreateToken>> reissue(@Auth AuthUser authUser, HttpServletRequest request) {
        return ApiResponse.onSuccess(SuccessStatus.OK, authService.reissue(authUser.getId(), request));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<String>> logout(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(SuccessStatus.OK, authService.logout(authUser.getId()));
    }

}