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


    public void storeValidation(Long storeId, Long userId){
        if (storeRepository.findByIdOrElseThrow(storeId).isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }
        if (!storeRepository.findByIdOrElseThrow(userId).getUser().getId().equals(userId)){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }
    }

    @Override
    @Transactional
    public void createMenu(
            AuthUser authUser, Long storeId, MenuDto.Request dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        //가게가 이미 삭제된 상태이거나 주인이 아닐 때
        storeValidation(storeId, authUser.getId());

        //메뉴가 이미 존재할 때
        if (menuRepository.existsByStoreAndName(store, dto.getName())) {
            throw new ApiException(ErrorStatus.MENU_ALREADY_EXISTS);
        }

        Menu createdMenu = new Menu(store, dto);
        menuRepository.save(createdMenu);
    }

    @Override
    @Transactional
    public void updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuDto.Request dto) {

        storeValidation(storeId, authUser.getId());

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        //해당 가게의 메뉴가 아닐 때
        if (!menu.getStore().getId().equals(storeId)){
            throw new ApiException(ErrorStatus.MENU_IS_NOT_IN_STORE);
        }
        //삭제된 메뉴일 때
        if (menu.isDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }

        menu.setMenu(dto);
    }

    @Override
    @Transactional
    public void deleteMenu(AuthUser authUser, Long storeId, Long menuId) {

        storeValidation(storeId, authUser.getId());

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        //해당 가게의 메뉴가 아닐 때
        if (!menu.getStore().getId().equals(storeId)){
            throw new ApiException(ErrorStatus.MENU_IS_NOT_IN_STORE);
        }
        //메뉴가 이미 삭제 상태일 때
        if (menu.getDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }

        menu.softDelete();
    }
}