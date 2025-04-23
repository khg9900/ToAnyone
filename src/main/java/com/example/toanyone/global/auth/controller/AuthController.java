package com.example.toanyone.global.auth.controller;

import com.example.toanyone.global.auth.dto.AuthRequestDto;
import com.example.toanyone.global.auth.dto.AuthResponseDto;
import com.example.toanyone.global.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public AuthResponseDto.CreateToken signup(@Valid @RequestBody AuthRequestDto.Signup signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/auth/signin")
    public AuthResponseDto.CreateToken signin(@Valid @RequestBody AuthRequestDto.Signin signin) {
        return authService.signin(signin);
    }
}
