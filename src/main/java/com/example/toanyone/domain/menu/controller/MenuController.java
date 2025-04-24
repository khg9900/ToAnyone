package com.example.toanyone.domain.menu.controller;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.service.MenuService;
import com.example.toanyone.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner/stores")
public class MenuController {
    private final MenuService menuService;
    private final StoreRepository storeRepository;

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<MenuDto.Response> createMenu(
            @PathVariable Long storeId,
            @Valid @RequestBody MenuDto.Request requestDto) {

        MainCategory mainCategory = MainCategory.of(requestDto.getMainCategory());
        SubCategory subCategory = SubCategory.of(requestDto.getSubCategory());

        MenuDto.Response response = menuService.createMenu(
                storeId,
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPrice(),
                mainCategory,
                subCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuDto.Response> updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody MenuDto.Request requestDto) {
        MainCategory mainCategory = MainCategory.of(requestDto.getMainCategory());
        SubCategory subCategory = SubCategory.of(requestDto.getSubCategory());

        MenuDto.Response response = menuService.updateMenu(
                storeId,
                menuId,
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPrice(),
                mainCategory,
                subCategory
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
