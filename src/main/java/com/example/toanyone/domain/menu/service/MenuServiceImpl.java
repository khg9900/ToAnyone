package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
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
            AuthUser authUser, Long storeId, String name, String description, Integer price,
            MainCategory mainCategory, SubCategory subCategory) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        if (!store.getUser().getId().equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        if (menuRepository.existsByStoreAndName(store, name)) {
            throw new ApiException(ErrorStatus.MENU_ALREADY_EXISTS);
        }

        Menu createdMenu = new Menu(store, name, description, price ,mainCategory, subCategory);
        menuRepository.save(createdMenu);
        log.info("Menu created: {}", createdMenu);

        return new MenuDto.Response("메뉴 생성되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response updateMenu(AuthUser authUser, Long storeId, Long menuId, String name, String description,
                                       Integer price, MainCategory mainCategory, SubCategory subCategory) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        if (!store.getUser().getId().equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        menu.setMenu(name, description, price, mainCategory, subCategory);
        log.info("Menu updated {}", menu);

        return new MenuDto.Response("메뉴 수정되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        if (!store.getUser().getId().equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        if (menu.getDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }
        menu.softDelete();
        return new MenuDto.Response("메뉴 삭제되었습니다");
    }
}
