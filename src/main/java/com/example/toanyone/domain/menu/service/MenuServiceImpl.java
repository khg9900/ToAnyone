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
            AuthUser authUser, Long storeId, MenuDto.Request dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        Long ownerId = store.getUser().getId();

        //가게가 이미 삭제된 상태일 때
        if (store.isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }

        //가게 주인이 아닐 때
        if (!ownerId.equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        //메뉴가 이미 존재할 때
        if (menuRepository.existsByStoreAndName(store, dto.getName())) {
            throw new ApiException(ErrorStatus.MENU_ALREADY_EXISTS);
        }

        Menu createdMenu = new Menu(store,
                dto.getName(), dto.getDescription(), dto.getPrice(),
                MainCategory.of(dto.getMainCategory()),
                SubCategory.of(dto.getSubCategory())
                );
        menuRepository.save(createdMenu);

        return new MenuDto.Response("메뉴 생성되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuDto.Request dto) {

        //가게 삭제 여부
        if (storeRepository.findByIdOrElseThrow(storeId).isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }

        Long ownerId = storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId);
        //가게의 주인이 아닐 때
        if (!ownerId.equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        //해당 가게의 메뉴가 아닐 때
        if (!menu.getStore().getId().equals(storeId)){
            throw new ApiException(ErrorStatus.MENU_IS_NOT_IN_STORE);
        }
        //삭제된 메뉴일 때
        if (menu.isDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }

        menu.setMenu(dto.getName(), dto.getDescription(), dto.getPrice(),
                MainCategory.of(dto.getMainCategory()), SubCategory.of(dto.getSubCategory()));

        return new MenuDto.Response("메뉴 수정되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        Long ownerId = storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId);
        //가게의 주인이 아닐 떄
        if (!ownerId.equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }
        //이미 삭제된 가게일 때
        if (storeRepository.findByIdOrElseThrow(storeId).isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }

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
        return new MenuDto.Response("메뉴 삭제되었습니다");
    }
}
