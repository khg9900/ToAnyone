package com.example.toanyone.domain.menu.controller;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.service.MenuService;
import com.example.toanyone.domain.store.repository.StoreRepository;
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
    public ResponseEntity<String> createMenu(
            @PathVariable Integer storeId,
            @RequestBody MenuDto.Request requestDto) {

        MenuDto.Response responseDto = menuService.createMenu(
                storeId,
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPrice(),
                requestDto.getMainCategory(),
                requestDto.getSubCategory());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto.getName()+" 메뉴가 추가되었습니다");

    }

}
