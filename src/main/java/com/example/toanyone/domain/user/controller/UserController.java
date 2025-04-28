package com.example.toanyone.domain.user.controller;

import com.example.toanyone.domain.user.dto.UserRequestDto;
import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.domain.user.service.UserService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto.Get>> getUserInfo(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(SuccessStatus.USER_FETCHED, userService.getUserInfo(authUser.getId()));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(@Auth AuthUser authUser, @RequestBody UserRequestDto.Update updateInfo) {
        userService.updateUserInfo(authUser.getId(), updateInfo);
        return ApiResponse.onSuccess(SuccessStatus.USER_UPDATED);
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Auth AuthUser authUser, @Valid @RequestBody UserRequestDto.ChangePassword changePassword) {
        userService.changePassword(authUser.getId(), changePassword);
        return ApiResponse.onSuccess(SuccessStatus.PASSWORD_UPDATE);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUserInfo(@Auth AuthUser authUser, @RequestBody UserRequestDto.Delete password) {
        userService.deleteUserInfo(authUser, password);
        return ApiResponse.onSuccess(SuccessStatus.USER_DELETED);
    }

}
