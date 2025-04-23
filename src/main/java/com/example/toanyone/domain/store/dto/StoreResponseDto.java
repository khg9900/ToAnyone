package com.example.toanyone.domain.store.dto;

import com.example.toanyone.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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
        private final LocalDateTime openTime;
        private final LocalDateTime closeTime;
        private final Integer deliveryFee;
        private final Integer minOrderPrice;
        private final String notice;
        private final String state;
        private final List<Menu> menus;
    }

    @Getter
    public static class Complete {
        private final String message;

        public Complete(String message) {
            this.message = message;
        }
    }
}
