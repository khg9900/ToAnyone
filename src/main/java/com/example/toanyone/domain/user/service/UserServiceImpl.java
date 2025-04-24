package com.example.toanyone.domain.user.service;

import static com.example.toanyone.global.common.code.ErrorStatus.*;

import com.example.toanyone.domain.user.dto.UserRequestDto;
import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.domain.user.dto.UserResponseDto.Get;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Get getUserInfo(Long authUserId) {

        User user = userRepository.findById(authUserId)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return new UserResponseDto.Get(user);
    }

    @Override
    @Transactional
    public void updateUserInfo(Long authUserId, UserRequestDto.Update updateInfo) {

        User user = userRepository.findById(authUserId)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        user.updateInfo(updateInfo);
    }
}
