package com.example.toanyone.domain.menu.controller;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.service.MenuService;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner/stores")
public class MenuController {
    private MenuService menuService;
    private StoreRepository storeRepository;

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
        return ResponseEntity.ok("메뉴 생성 완료되었습니다");

    }

}
