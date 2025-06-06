package com.example.toanyone.domain.menu.controller;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.service.MenuService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner/stores")
public class MenuController {
    private final MenuService menuService;

    /*
    1. createMenu
      : 사용자 정보, store_id, 생성할 메뉴의 정보를 받아 menuService로 전달
    2. updateMenu
      : 사용자 정보, store_id, menu_id, 수정할 메뉴의 정보를 받아 menuService로 전달
    3. deleteMenu
      : 사용자 정보, store_id, menu_id를 받아 menuService로 전달
     */

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<ApiResponse<Void>> createMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody MenuDto.Request requestDto
    ) {
        menuService.createMenu(authUser,storeId, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.MENU_CREATED);
    }

    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<Void>> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuDto.Request requestDto
    ) {
        menuService.updateMenu(authUser, storeId, menuId, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.MENU_UPDATED);
    }

    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ){
        menuService.deleteMenu(authUser, storeId, menuId);
        return ApiResponse.onSuccess(SuccessStatus.MENU_DELETED);
    }
}