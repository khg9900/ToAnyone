package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class GetStoreTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    void 내가_운영중인가게를_조회한다() {
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(ownerId, "eee@gmail.com", UserRole.OWNER);

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "email", "eee@gmail.com");
        ReflectionTestUtils.setField(user, "userRole", UserRole.OWNER);

        Store store1 = new Store();
        ReflectionTestUtils.setField(store1, "user", user);
        ReflectionTestUtils.setField(store1, "id", 1L);
        ReflectionTestUtils.setField(store1, "name", "가게1");

        Store store2 = new Store();
        ReflectionTestUtils.setField(store2, "user", user);
        ReflectionTestUtils.setField(store2, "id", 2L);
        ReflectionTestUtils.setField(store2, "name", "가게2");

        List<Store> stores = List.of(store1, store2);

        // GIVEN
        given(userRepository.findById(ownerId)).willReturn(Optional.of(user));
        given(storeRepository.countByUserIdAndDeletedFalse(1L)).willReturn(2);
        given(storeRepository.findByUserIdAndDeletedFalse(1L)).willReturn(stores);

        // WHEN
        List<StoreResponseDto.GetAll> response = storeService.getStoresByOwner(ownerId);

        // THEN
        assertEquals(2, response.size());
        assertEquals(store1.getId(), response.get(0).getStoreId());
        assertEquals(store1.getName(), response.get(0).getName());
        assertEquals(store2.getId(), response.get(1).getStoreId());
        assertEquals(store2.getName(), response.get(1).getName());

    }

    @Test
    void 내가_운영중인가게가_없다() {
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(ownerId, "eee@gmail.com", UserRole.OWNER);

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "email", "eee@gmail.com");
        ReflectionTestUtils.setField(user, "userRole", UserRole.OWNER);

        // GIVEN
        given(userRepository.findById(ownerId)).willReturn(Optional.of(user));
        given(storeRepository.countByUserIdAndDeletedFalse(1L)).willReturn(0);
        given(storeRepository.findByUserIdAndDeletedFalse(1L)).willReturn(null);

        // WHEN
        ApiException apiException = assertThrows(ApiException.class,
                () -> storeService.getStoresByOwner(ownerId));

        // THEN
        assertEquals(ErrorStatus.STORE_NOT_FOUND.getMessage(),apiException.getMessage());

    }

    @Test
    void 키워드가_포함된_가게목록을_조회한다() {
        Store store1 = new Store();
        ReflectionTestUtils.setField(store1, "id", 1L);
        ReflectionTestUtils.setField(store1, "name", "치킨가게1");

        Store store2 = new Store();
        ReflectionTestUtils.setField(store2, "id", 2L);
        ReflectionTestUtils.setField(store2, "name", "피자가게2");

        Store store3 = new Store();
        ReflectionTestUtils.setField(store3, "id", 3L);
        ReflectionTestUtils.setField(store3, "name", "치킨가게3");

        List<Store> stores = List.of(store1, store3);

        String keyword = "치킨";

        // GIVEN
        given(storeRepository.findByNameContainingAndDeletedFalse(keyword)).willReturn(stores);

        // WHEN
        List<StoreResponseDto.GetAll> response = storeService.getStoresByName(keyword);

        // THEN
        assertEquals(2, response.size());
        assertEquals(store1.getId(), response.get(0).getStoreId());
        assertEquals(store1.getName(), response.get(0).getName());
        assertEquals(store3.getId(), response.get(1).getStoreId());
        assertEquals(store3.getName(), response.get(1).getName());

    }

    @Test
    void 키워드가_포함된_가게명이_없다() {
        Store store1 = new Store();
        ReflectionTestUtils.setField(store1, "id", 1L);
        ReflectionTestUtils.setField(store1, "name", "치킨가게");

        Store store2 = new Store();
        ReflectionTestUtils.setField(store2, "id", 2L);
        ReflectionTestUtils.setField(store2, "name", "피자가게");

        List<Store> stores = List.of();

        String keyword = "김밥";

        // GIVEN
        given(storeRepository.findByNameContainingAndDeletedFalse(keyword)).willReturn(stores);

        // WHEN
        ApiException apiException = assertThrows(ApiException.class,
                () -> storeService.getStoresByName(keyword));

        // THEN
        assertEquals(ErrorStatus.STORE_SEARCH_NO_MATCH.getMessage(), apiException.getMessage());

    }

    @Test
    void 한가게의_정보를_조회한다() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        
        StoreRequestDto.Create requestDto = StoreRequestDto.Create.builder()
                .name("가게명")
                .address("가게주소")
                .openTime("10:00")
                .closeTime("18:00")
                .deliveryFee(3000)
                .minOrderPrice(10000)
                .notice("가게공지")
                .status("OPEN")
                .phone("02-123-4567")
                .build();
        
        LocalTime openTime = LocalTime.parse("10:00");
        LocalTime closeTime = LocalTime.parse("18:00");

        Store store = new Store(user, requestDto, Status.OPEN,openTime, closeTime);
        ReflectionTestUtils.setField(store, "id", 1L);

        // GIVEN
        given(storeRepository.findByIdOrElseThrow(1L)).willReturn(store);

        // WHEN
        StoreResponseDto.GetById response = storeService.getStoreById(1L);

        // THEN
        assertSoftly(softly -> {
            softly.assertThat(response.getStoreId()).isEqualTo(1L);
            softly.assertThat(response.getOwnerId()).isEqualTo(1L);
            softly.assertThat(response.getName()).isEqualTo("가게명");
            softly.assertThat(response.getAddress()).isEqualTo("가게주소");
            softly.assertThat(response.getOpenTime()).isEqualTo(LocalTime.parse("10:00"));
            softly.assertThat(response.getCloseTime()).isEqualTo(LocalTime.parse("18:00"));
            softly.assertThat(response.getDeliveryFee()).isEqualTo(3000);
            softly.assertThat(response.getMinOrderPrice()).isEqualTo(10000);
            softly.assertThat(response.getNotice()).isEqualTo("가게공지");
            softly.assertThat(response.getStatus()).isEqualTo(Status.OPEN);
            softly.assertThat(response.getPhone()).isEqualTo("02-123-4567");

        });

//        // given
//        StoreResponseDto.GetById response = storeService.getStoreById(1L);
//
//        // then
//        assertSoftly(softly -> {
//            softly.assertThat(response.getStoreId()).isEqualTo(1L);
//            softly.assertThat(response.getOwnerId()).isEqualTo(1L);
//            softly.assertThat(response.getName()).isEqualTo("가게명");
//            softly.assertThat(response.getAddress()).isEqualTo("가게주소");
//            softly.assertThat(response.getOpenTime()).isEqualTo(LocalTime.parse("10:00"));
//            softly.assertThat(response.getCloseTime()).isEqualTo(LocalTime.parse("18:00"));
//            softly.assertThat(response.getDeliveryFee()).isEqualTo(3000);
//            softly.assertThat(response.getMinOrderPrice()).isEqualTo(10000);
//            softly.assertThat(response.getNotice()).isEqualTo("가게공지");
//            softly.assertThat(response.getStatus()).isEqualTo(Status.OPEN);
//            softly.assertThat(response.getPhone()).isEqualTo("02-123-4567");
//        });

    }

}
