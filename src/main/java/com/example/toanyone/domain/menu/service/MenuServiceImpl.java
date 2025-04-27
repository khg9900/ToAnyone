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

    @Transactional
    public MainCategory toMainCategory(String mainCategory) {
        return MainCategory.of(mainCategory);
    }
    @Transactional
    public SubCategory toSubCategory(String subCategory) {
        return SubCategory.of(subCategory);
    }


    @Override
    @Transactional
    public MenuDto.Response createMenu(
            AuthUser authUser, Long storeId, MenuDto.Request dto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        Long ownerId = store.getUser().getId();

        if (store.isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }

        if (!ownerId.equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        if (menuRepository.existsByStoreAndName(store, dto.getName())) {
            throw new ApiException(ErrorStatus.MENU_ALREADY_EXISTS);
        }

        Menu createdMenu = new Menu(store,
                dto.getName(), dto.getDescription(), dto.getPrice(),
                toMainCategory(dto.getMainCategory()),
                toSubCategory(dto.getSubCategory())
                );
        menuRepository.save(createdMenu);
        log.info("Menu created: {}", createdMenu);

        return new MenuDto.Response("메뉴 생성되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuDto.Request dto) {

        if (storeRepository.findByIdOrElseThrow(storeId).isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }
        Long ownerId = storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId);

        if (!ownerId.equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        if (menu.isDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }

        menu.setMenu(dto.getName(), dto.getDescription(), dto.getPrice(),
                toMainCategory(dto.getMainCategory()), toSubCategory(dto.getSubCategory()));

        return new MenuDto.Response("메뉴 수정되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        Long ownerId = storeRepository.findOwnerIdByStoreIdOrElseThrow(storeId);
        if (!ownerId.equals(authUser.getId())){
            throw new ApiException(ErrorStatus.NOT_STORE_OWNER);
        }
        if (storeRepository.findByIdOrElseThrow(storeId).isDeleted()){
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }
        if (menuRepository.findByIdOrElseThrow(menuId).isDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        if (menu.getDeleted()){
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }
        menu.softDelete();
        return new MenuDto.Response("메뉴 삭제되었습니다");
    }
}
