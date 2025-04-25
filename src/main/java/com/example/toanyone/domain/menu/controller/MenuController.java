package com.example.toanyone.domain.menu.controller;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
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

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<ApiResponse<MenuDto.Response>> createMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody MenuDto.Request requestDto) {

        MainCategory mainCategory = MainCategory.of(requestDto.getMainCategory());
        SubCategory subCategory = SubCategory.of(requestDto.getSubCategory());

        MenuDto.Response response = menuService.createMenu(
                authUser,
                storeId,
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPrice(),
                mainCategory,
                subCategory);

        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }

    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuDto.Response>> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuDto.Request requestDto) {
        MainCategory mainCategory = MainCategory.of(requestDto.getMainCategory());
        SubCategory subCategory = SubCategory.of(requestDto.getSubCategory());

        MenuDto.Response response = menuService.updateMenu(
                authUser,
                storeId,
                menuId,
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPrice(),
                mainCategory,
                subCategory
        );

        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuDto.Response>> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId){

        MenuDto.Response response = menuService.deleteMenu(authUser, storeId, menuId);

        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

}
