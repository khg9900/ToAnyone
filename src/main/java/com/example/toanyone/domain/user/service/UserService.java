package com.example.toanyone.domain.user.service;

import com.example.toanyone.domain.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto.Get getUserInfo(Long authUserId);

}
