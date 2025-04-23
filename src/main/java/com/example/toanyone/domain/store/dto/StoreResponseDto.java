package com.example.toanyone.domain.store.dto;

import com.example.toanyone.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResponseDto {

    @Getter
    public static class GetAll {
        private Long storeId;
        private String name;
    }

    @Getter
    public static class GetById {
        private Long storeId;
        private Long ownerId;
        private String name;
        private String address;
        private LocalDateTime openTime;
        private LocalDateTime closeTime;
        private Integer deliveryFee;
        private Integer minOrderPrice;
        private String notice;
        private String state;
        private List<Menu> menus;
    }

    @Getter
    public static class Complete {
        private String message;

        public Complete(String message) {
            this.message = message;
        }
    }
}
