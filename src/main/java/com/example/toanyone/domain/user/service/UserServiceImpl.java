package com.example.toanyone.domain.user.service;

import static com.example.toanyone.global.common.code.ErrorStatus.*;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.repository.CartRepository;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.dto.UserRequestDto;
import com.example.toanyone.domain.user.dto.UserResponseDto;
import com.example.toanyone.domain.user.dto.UserResponseDto.Get;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.error.ApiException;
import com.example.toanyone.global.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;

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

    @Override
    @Transactional
    public void deleteUserInfo(AuthUser authUser, UserRequestDto.Delete password) {

        User user = userRepository.findById(authUser.getId())
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        // 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(password.getPassword(), user.getPassword())) {
            throw new ApiException(INVALID_PASSWORD);
        }

        // 사장님은 회원 정보를 삭제하기 전 운영중인 가게가 없어야 합니다.
        if (authUser.getUserRole().equals(UserRole.OWNER)) {
            // 운영중인 가게 확인
            int countStore = storeRepository.countByUserIdAndDeletedFalse(authUser.getId());
            // 회원 탈퇴 불가
            if (countStore != 0) {
                throw new ApiException(OWNER_HAS_ACTIVE_STORE);
            }
        }

        // 회원과 연결된 장바구니도 함께 삭제됩니다.
            // 장바구니 비우기 API 완성 후 작성 예정.

        user.softDelete();
    }
}
