package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
public class CreateStoreTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    void 가게_생성_성공() {

        Long ownerId = 1L;
        User user = new User(
                "eeeee@gmail.com",
                "1234Aaa.",
                "이름",
                UserRole.OWNER,
                "별명",
                "010-1234-1234",
                "주소",
                Gender.FEMALE,
                LocalDate.of(2000, 1, 1),
                26);

        StoreRequestDto.Create reqest = StoreRequestDto.Create.builder()
                .name("가게이름")
                .address("주소")
                .openTime("11:00")
                .closeTime("21:00")
                .deliveryFee(3000)
                .minOrderPrice(10000)
                .notice("공지")
                .status(String.valueOf(Status.OPEN))
                .phone("02-123-4567")
                .build();

        //GIVEN
        given(userRepository.findById(ownerId)).willReturn(Optional.of(user));
        given(storeRepository.countByUserIdAndDeletedFalse(ownerId)).willReturn(1);
        given(storeRepository.existsByName(reqest.getName())).willReturn(false);

        //WHEN
        StoreResponseDto.Complete responseDto = storeService.createStore(ownerId, reqest);

        //THEN
        assertEquals("가게가 생성되었습니다.", responseDto.getMessage());

    }

    @Test
    void 가게는_3개까지_만들수있다() {

        Long ownerId = 1L;
        User user = new User(
                "eeeee@gmail.com",
                "1234Aaa.",
                "이름",
                UserRole.OWNER,
                "별명",
                "010-1234-1234",
                "주소",
                Gender.FEMALE,
                LocalDate.of(2000, 1, 1),
                26);

        StoreRequestDto.Create reqest = StoreRequestDto.Create.builder()
                .name("가게이름")
                .address("주소")
                .openTime("11:00")
                .closeTime("21:00")
                .deliveryFee(3000)
                .minOrderPrice(10000)
                .notice("공지")
                .status(String.valueOf(Status.OPEN))
                .phone("02-123-4567")
                .build();

        //GIVEN
        given(userRepository.findById(ownerId)).willReturn(Optional.of(user));
        given(storeRepository.countByUserIdAndDeletedFalse(ownerId)).willReturn(3);

        //WHEN
        ApiException apiException = assertThrows(ApiException.class, () -> storeService.createStore(ownerId, reqest));

        //THEN
        assertEquals("가게는 최대 3개까지 등록 가능합니다.", apiException.getMessage());
    }

}


