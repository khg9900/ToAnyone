package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.store.service.StoreService;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuServiceImpl menuService;




    @Test
    void 가게_주인이_메뉴_생성을_정상적으로_하면_성공한다() {
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);

        Store store = new Store();

        // given
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);

        // when
        MenuDto.Response response = menuService.createMenu(authUser, storeId,
                "menu", "description", 1000, MainCategory.KOREAN, SubCategory.DRINK);

        // then
        assertEquals("메뉴 생성되었습니다", response.getMessage());
    }

}
