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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class GetStoreByOwner {
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
}
