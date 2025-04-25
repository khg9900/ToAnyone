package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.global.auth.dto.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface MenuService {

    MenuDto.Response createMenu(AuthUser authUser, Long storeId, String name, String description,
                                Integer price, MainCategory mainCategory, SubCategory subCategory);

    MenuDto.Response updateMenu(AuthUser authUser, Long storeId, Long menuId, String name,
                                String description, Integer price, MainCategory mainCategory, SubCategory subCategory);

    MenuDto.Response deleteMenu(AuthUser authUser, Long storeId, Long menuId);
}