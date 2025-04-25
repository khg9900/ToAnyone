package com.example.toanyone.domain.store.dto;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StoreResponseDto {

    @Getter
    @AllArgsConstructor
    public static class GetAll {
        private final Long storeId;
        private final String name;

    }

    @Getter
    @AllArgsConstructor
    public static class GetById {
        private final Long storeId;
        private final Long ownerId;
        private final String name;
        private final String address;
        private final LocalTime openTime;
        private final LocalTime closeTime;
        private final Integer deliveryFee;
        private final Integer minOrderPrice;
        private final String notice;
        private final Status status;
        private final String phone;
        private final List<MenuDto.ResponseDetail> menus;

        public GetById(Store store) {
            this.storeId = store.getId();
            this.ownerId = store.getUser().getId();
            this.name = store.getName();
            this.address = store.getAddress();
            this.openTime = store.getOpenTime();
            this.closeTime = store.getCloseTime();
            this.deliveryFee = store.getDeliveryFee();
            this.minOrderPrice = store.getMinOrderPrice();
            this.notice = store.getNotice();
            this.status = store.getStatus();
            this.phone = store.getPhone();

            // 메뉴 리스트
            List<MenuDto.ResponseDetail> menuList = new ArrayList<>();
            for (Menu menu : store.getMenus()) {
                MenuDto.ResponseDetail dto = new MenuDto.ResponseDetail(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.getDescription(),
                        menu.getMainCategory(),
                        menu.getSubCategory()
                );

                menuList.add(dto);
            }

            this.menus = menuList;
        }
    }

    @Getter
    public static class Complete {
        private final String message;

        public Complete(String message) {
            this.message = message;
        }
    }
}
