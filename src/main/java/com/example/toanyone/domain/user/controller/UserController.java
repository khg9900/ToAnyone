package com.example.toanyone.domain.user.controller;

import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.domain.user.service.UserService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto.Get>> getUserInfo(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(SuccessStatus.OK, userService.getUserInfo(authUser.getId()));
    }

}
