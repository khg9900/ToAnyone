package com.example.toanyone.domain.user.service;

import static com.example.toanyone.global.common.code.ErrorStatus.*;

import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.domain.user.dto.UserResponseDto.Get;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
