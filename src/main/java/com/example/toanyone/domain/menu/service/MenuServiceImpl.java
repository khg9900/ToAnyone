package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Override
    public MenuDto.Response createMenu(
            Integer storeId, String name, String description, Integer price,
            MainCategory mainCategory, SubCategory subCategory) {


        Menu menu = new Menu();

        return null;
    }
}
