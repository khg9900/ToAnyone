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
            Long storeId, String name, String description, Integer price,
            MainCategory mainCategory, SubCategory subCategory) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if (menuRepository.existsByStoreAndName(store, name)) {
            throw new RuntimeException("이 가게에는 이미 같은 이름의 메뉴가 존재합니다");
        }

        Menu createdMenu = new Menu(store, name, description, price ,mainCategory, subCategory);
        menuRepository.save(createdMenu);
        log.info("Menu created: {}", createdMenu);

        return new MenuDto.Response("메뉴 생성되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response updateMenu(Long storeId, Long menuId, String name, String description,
                                       Integer price, MainCategory mainCategory, SubCategory subCategory) {
        storeRepository.findByIdOrElseThrow(storeId);
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        menu.setMenu(name, description, price, mainCategory, subCategory);
        log.info("Menu updated {}", menu);

        return new MenuDto.Response("메뉴 수정되었습니다");
    }

    @Override
    @Transactional
    public MenuDto.Response deleteMenu(Long storeId, Long menuId) {
        storeRepository.findByIdOrElseThrow(storeId);
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        if (menu.getDeleted()){
            throw new RuntimeException("이미 삭제된 메뉴입니다"); //글로벌 익셉션 만든 후 수정
        }
        menu.softDelete();
        return new MenuDto.Response("메뉴 삭제되었습니다");
    }
}
