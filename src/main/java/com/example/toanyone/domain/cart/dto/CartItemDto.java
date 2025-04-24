package com.example.toanyone.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CartItemDto {
    @Getter
    @NoArgsConstructor
    public static class CartItems {
        private Long menuId;
        private String menuName;
        private Integer menuPrice;
        private Integer quantity;
        private Integer totalPrice;

        public CartItems(Long menuId, String menuName, Integer menuPrice,
                         Integer quantity, Integer totalPrice) {
            this.menuId = menuId;
            this.menuName = menuName;
            this.menuPrice = menuPrice;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String storeName;
        private List<CartItems> items;
        private Integer orderPrice;
        private Integer deliveryFee;
        private Integer totalPrice;
    }
}
