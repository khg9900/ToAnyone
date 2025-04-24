package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public MenuDto.Response createMenu(
            Integer storeId, String name, String description, Integer price,
            MainCategory mainCategory, SubCategory subCategory) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Menu createdMenu = new Menu(store, name, description, price ,mainCategory, subCategory);
        menuRepository.save(createdMenu);
        log.info("Menu created: {}", createdMenu);

        return new MenuDto.Response("메뉴 생성되었습니다");
    }
}
