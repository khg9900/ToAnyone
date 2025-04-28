package com.example.toanyone.domain.menu.controller;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.service.MenuServiceImpl;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.error.ApiException;
import com.example.toanyone.global.common.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MenuControllerTest {
    @Mock
    private MenuServiceImpl menuService;
    @InjectMocks
    private MenuController menuController;


    /*
    1. addMenu
      (1) 메뉴 생성 성공
      (2) 존재하지 않는 카테고리 이름일 때
     */
    @Test
    void 메뉴_생성에_성공했을_때(){
        //given
        AuthUser authUser = mock(AuthUser.class);
        Long storeId = 1L;
        MenuDto.Request requestDto = new MenuDto.Request(
                "name", "description", 1000, "KOREAN", "DRINK"
        );
        // when
        ResponseEntity<ApiResponse<Void>> response =
                menuController.createMenu(authUser, storeId, requestDto);

        //then
        assertEquals("201 CREATED", response.getStatusCode().toString());
    }



    /*
    2. 메뉴 수정하기
*/
    @Test
    void 메뉴를_정상적으로_수정(){
        AuthUser authUser = mock(AuthUser.class);
        Long storeId = 1L;
        MenuDto.Request requestDto = new MenuDto.Request(
                "name", "description", 1000, "KOREAN", "DRINK"
        );
        Long menuId = 1L;

        ResponseEntity<ApiResponse<Void>> response =
                menuController.updateMenu(authUser, storeId,
                        menuId, requestDto);


        assertEquals("204 NO_CONTENT", response.getStatusCode().toString());
    }

    /*
    3. 메뉴 삭제
     */

    @Test
    void 메뉴를_정상적으로_삭제(){
        AuthUser authUser = mock(AuthUser.class);
        Long storeId = 1L;

        Long menuId = 1L;

        ResponseEntity<ApiResponse<Void>> response =
                menuController.deleteMenu(authUser, storeId,menuId);


        assertEquals("204 NO_CONTENT", response.getStatusCode().toString());
    }


}
