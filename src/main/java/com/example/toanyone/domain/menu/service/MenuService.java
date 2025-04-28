package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.global.auth.dto.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface MenuService {

    void createMenu(AuthUser authUser, Long storeId, MenuDto.Request requestDto);

    void updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuDto.Request requestDto);

    void deleteMenu(AuthUser authUser, Long storeId, Long menuId);
}