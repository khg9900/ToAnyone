package com.example.toanyone.domain.store.dto;

import com.example.toanyone.domain.menu.dto.MenuDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
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
        private final String state;
        private final List<MenuDto.ResponseDetail> menus;
    }

    @Getter
    public static class Complete {
        private final String message;

        public Complete(String message) {
            this.message = message;
        }
    }
}
