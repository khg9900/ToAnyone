package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuServiceImpl menuService;


    /*
     1. createMenu 메서드 테스트
       (1) 성공 했을 때
       (2) 가게 주인이 아닐 때
       (3) 이미 있는 메뉴일 때
     */


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


    @Test
    void 가게_주인이_아니면_생성을_못한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        Long anotherOwnerId = 2L;

        AuthUser authUser = new AuthUser(anotherOwnerId, "kkk@gmail.com", UserRole.OWNER);
        Store store = new Store();

        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);

        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.createMenu(authUser, storeId,"menu", "description", 1000, MainCategory.KOREAN, SubCategory.DRINK));

        assertEquals("가게의 주인이 아니면 접근할 수 없습니다.", apiException.getMessage());
    }


    @Test
    void 이미_있는_메뉴는_추가할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);
        Store store = new Store();

        //given
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);
        given(menuRepository.existsByStoreAndName(store, "menu")).willReturn(true);

        //when
        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.createMenu(authUser, storeId,"menu", "description", 1000, MainCategory.KOREAN, SubCategory.DRINK));

        //then
        assertEquals("이미 존재하는 메뉴입니다.", apiException.getMessage());
    }

    /*
     2. updateMenu 메서드 테스트
       (1) 성공 했을 때
       (2) 가게 주인이 아닐 때
       (3) 없는 메뉴일 때
     */

    @Test
    void 가게_주인이_정상적으로_존재하는_메뉴를_수정한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);
        Long menuId = 1L;
        Menu menu = new Menu();

        //given
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);

        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);
        //when
        MenuDto.Response response = menuService.updateMenu(authUser, 1L ,menuId, "menu", "description", 1000, MainCategory.KOREAN, SubCategory.DRINK);
        //then
        assertEquals("메뉴 수정되었습니다", response.getMessage());
    }

    @Test
    void 가게_주인이_아니면_수정을_못한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        Long anotherOwnerId = 2L;

        AuthUser authUser = new AuthUser(anotherOwnerId, "kkk@gmail.com", UserRole.OWNER);

        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);


        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.updateMenu(authUser, storeId,1L,"menu", "description", 1000, MainCategory.KOREAN, SubCategory.DRINK));

        assertEquals("가게의 주인이 아니면 접근할 수 없습니다.", apiException.getMessage());
    }

    @Test
    void 없는_메뉴는_수정할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);
        Long menuId = 1L;
        //given
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);

        given(menuRepository.findByIdOrElseThrow(menuId))
                .willThrow(new ApiException(ErrorStatus.MENU_NOT_FOUND));
        //when
        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.updateMenu(authUser, storeId,menuId,"menu", "description", 1000, MainCategory.KOREAN, SubCategory.DRINK));
        //then
        assertEquals("존재하지 않는 메뉴입니다.", apiException.getMessage());
    }

    /*
     3. deleteMenu 메서드 테스트
       (1) 성공 했을 때
       (2) 가게 주인이 아닐 때
       (3) 없는 메뉴일 때
       (4) 이미 삭제된 메뉴일 때
     */

    @Test
    void 가게_주인이_메뉴를_정상적으로_삭제한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);
        Long menuId = 1L;
        Menu menu = new Menu();

        //given
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);

        //when
        MenuDto.Response response = menuService.deleteMenu(authUser,storeId,menuId);

        //then
        assertEquals("메뉴 삭제되었습니다", response.getMessage());
    }


    @Test
    void 가게_주인이_아니면_삭제를_못한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        Long anotherOwnerId = 2L;

        AuthUser authUser = new AuthUser(anotherOwnerId, "kkk@gmail.com", UserRole.OWNER);

        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);


        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.deleteMenu(authUser, storeId,1L));

        assertEquals("가게의 주인이 아니면 접근할 수 없습니다.", apiException.getMessage());
    }

    @Test
    void 없는_메뉴는_삭제할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);
        Long menuId = 1L;
        //given
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);

        given(menuRepository.findByIdOrElseThrow(menuId))
                .willThrow(new ApiException(ErrorStatus.MENU_NOT_FOUND));
        //when
        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.deleteMenu(authUser, storeId,menuId));
        //then
        assertEquals("존재하지 않는 메뉴입니다.", apiException.getMessage());
    }

    @Test
    void 삭제된_메뉴는_삭제할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", UserRole.OWNER);
        Menu menu = mock(Menu.class);
        //given
        given(storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId)).willReturn(ownerId);
        given(menuRepository.findByIdOrElseThrow(menu.getId())).willReturn(menu);
        given(menu.getDeleted()).willReturn(true);

        //when

        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.deleteMenu(authUser, storeId,menu.getId()));
        //then
        assertEquals("이미 삭제된 메뉴입니다.", apiException.getMessage());
    }





}

