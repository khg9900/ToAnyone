package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.time.LocalDate;

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
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                 "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());

        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);

        MenuDto.Request requestDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        // given
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.existsByStoreAndName(store, requestDto.getName())).willReturn(false);

        // when
        MenuDto.Response response = menuService.createMenu(authUser, storeId, requestDto);

        // then
        assertEquals("메뉴 생성되었습니다", response.getMessage());
    }


    @Test
    void 가게_주인이_아니면_생성을_못한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(2L, "kkk@gmail.com", "OWNER");

        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());

        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);

        MenuDto.Request requestDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);

        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.createMenu(authUser, storeId,requestDto));

        assertEquals("가게의 주인이 아니면 접근할 수 없습니다.", apiException.getMessage());
    }
    @Test
    void 폐업한_가게에는_접근을_못한다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(2L, "kkk@gmail.com", "OWNER");

        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());

        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "deleted", true);

        MenuDto.Request requestDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);

        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.createMenu(authUser, storeId,requestDto));

        assertEquals("폐업한 가게입니다.", apiException.getMessage());
    }



    @Test
    void 이미_있는_메뉴는_추가할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());

        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);

        MenuDto.Request requestDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        //given
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.existsByStoreAndName(store, "menu")).willReturn(true);

        //when
        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.createMenu(authUser, storeId,requestDto));

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
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");
        MenuDto.Request menuUpdateDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");


        //Owner 설정
        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());
            //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        //Store 생성 및 id 강제 부여
        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "id", storeId);

        //Menu 생성 및 id 강제 부여
        MenuDto.Request originDto = new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK");
        Menu menu = new Menu(store, originDto);
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);

        //when
        MenuDto.Response response = menuService.updateMenu(authUser, storeId ,menu.getId(), menuUpdateDto);
        //then
        assertEquals("메뉴 수정되었습니다", response.getMessage());
    }

    @Test
    void 가게의_메뉴가_아니면_수정을_못한다(){
        Long storeId = 1L;
        Long anotherStoreId = 2L;
        Long ownerId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");
        MenuDto.Request menuUpdateDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        //Owner 설정
        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());
        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        //Store 생성 및 id 강제 부여
        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "id", storeId);

        Store anotherStore = new Store(owner, dto);
        ReflectionTestUtils.setField(anotherStore, "id", anotherStoreId);

        //Menu 생성 및 id 강제 부여
        MenuDto.Request originDto = new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK");
        Menu menu = new Menu(anotherStore, originDto);
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);


        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.updateMenu(authUser, storeId,menuId, menuUpdateDto));

        assertEquals("해당 가게에 존재하지 않는 메뉴입니다", apiException.getMessage());
    }

    @Test
    void 삭제된_메뉴는_수정할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");
        MenuDto.Request menuUpdateDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        //Owner 설정
        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());
        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        //Store 생성 및 id 강제 부여
        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "id", storeId);

        //Menu 생성 및 id 강제 부여
        MenuDto.Request originDto = new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK");
        Menu menu = new Menu(store, originDto);
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "deleted", true);


        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);

        //when
        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.updateMenu(authUser, storeId,menuId,menuUpdateDto));
        //then
        assertEquals("이미 삭제된 메뉴입니다.", apiException.getMessage());
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
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");
        MenuDto.Request menuUpdateDto = new MenuDto.Request("menu", "description", 1000, "KOREAN", "DRINK");

        //Owner 설정
        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());
        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        //Store 생성 및 id 강제 부여
        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "id", storeId);

        //Menu 생성 및 id 강제 부여
        MenuDto.Request originDto = new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK");
        Menu menu = new Menu(store, originDto);
        ReflectionTestUtils.setField(menu, "id", menuId);

        //given
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);

        //when
        MenuDto.Response response = menuService.deleteMenu(authUser,storeId,menuId);

        //then
        assertEquals("메뉴 삭제되었습니다", response.getMessage());
    }


    @Test
    void 가게의_메뉴가_아니면_삭제를_못한다(){
        Long storeId = 1L;
        Long anotherStoreId = 2L;
        Long ownerId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        //Owner 설정
        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());
        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        //Store 생성 및 id 강제 부여
        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "id", storeId);

        Store anotherStore = new Store(owner, dto);
        ReflectionTestUtils.setField(anotherStore, "id", anotherStoreId);

        //Menu 생성 및 id 강제 부여
        MenuDto.Request originDto = new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK");
        Menu menu = new Menu(anotherStore, originDto);
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);


        ApiException apiException = assertThrows(ApiException.class,
                () -> menuService.deleteMenu(authUser, storeId,menuId));
        //then
        assertEquals("해당 가게에 존재하지 않는 메뉴입니다", apiException.getMessage());
    }


    @Test
    void 삭제된_메뉴는_삭제할_수_없다(){
        Long storeId = 1L;
        Long ownerId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");
        //Owner 설정
        User owner = new User("kkk@gmail.com", "111Aaa.", "name", UserRole.OWNER,
                "nick", "010-4444-0000", "address", "MALE", LocalDate.of(2002,12, 18).toString());
        //Owner의 id를 강제로 설정
        ReflectionTestUtils.setField(owner, "id", ownerId);

        //Store 생성 및 id 강제 부여
        StoreRequestDto.Create dto = new StoreRequestDto.Create("name", "address",
                "10:00", "21:00", 1000, 1000, "notice", "OPEN", "000-0000-0000");
        Store store = new Store(owner, dto);
        ReflectionTestUtils.setField(store, "id", storeId);

        //Menu 생성 및 id 강제 부여
        MenuDto.Request originDto = new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK");
        Menu menu = new Menu(store, originDto);
        ReflectionTestUtils.setField(menu, "id", menuId);
        ReflectionTestUtils.setField(menu, "deleted", true);


        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);

        //when
        ApiException apiException = assertThrows(ApiException.class,
                ()-> menuService.deleteMenu(authUser, storeId,menuId));
        //then
        assertEquals("이미 삭제된 메뉴입니다.", apiException.getMessage());
    }





}
