package com.example.toanyone.domain.user.service;

import com.example.toanyone.domain.user.dto.UserRequestDto;
import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.global.auth.dto.AuthUser;

public interface UserService {

    UserResponseDto.Get getUserInfo(Long authUserId);

    void updateUserInfo(Long authUserId, UserRequestDto .Update updateInfo);

    void deleteUserInfo(AuthUser authUser, UserRequestDto.Delete password);

}
