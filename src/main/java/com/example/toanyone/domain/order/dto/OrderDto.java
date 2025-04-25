package com.example.toanyone.domain.order.dto;
import com.example.toanyone.domain.order.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    //
    // 주문 생성 요청 DTO
    //
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotNull
        private Long cartId;
    }


    //
    // 주문 상태 변경 요청 DTO (사장님)
    //
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {

        @NotNull(message = "주문 상태는 필수입니다.")
        private OrderStatus status;
    }

    //
    // 주문 상태 변경 응답 DTO (사장님)
    //
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateResponse {
        private String message;
    }

    //
    // 사장님 - 가게 주문 목록 조회 응답
    //
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OwnerOrderSummary {
        private Long orderId;
        private String nickName;
        private String status;
        private LocalDateTime createdAt;
    }

    //
    // 고객 - 내 주문 내역 목록 조회 응답
    //
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserOrderDetail {
        private Long orderId;
        private String storeName;
        private String status;
        private Integer orderPrice;
        private Integer deliveryFee;
        private Integer totalPrice;
        private LocalDateTime createdAt;
        private List<OrderItemDetail> items;
    }

    //
    // 고객 - 주문 내역 메뉴 항목
    //
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDetail {
        private String menuName;
        private Integer price;
        private Integer count;
    }

    // 주문 생성 응답
    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private Long orderId;
        private LocalDateTime createdAt;
        private String status;
    }

    // 사장님 주문 목록 조회 응답
    @Getter
    @AllArgsConstructor
    public static class OwnerOrderListResponse {
        private Long orderId;
        private String nickName;
        private String status;
        private LocalDateTime createdAt;
    }

    // 사용자 주문 내역 조회 응답
    @Getter
    @AllArgsConstructor
    public static class UserOrderHistoryResponse {
        private Long orderId;
        private String storeName;
        private String status;
        private Integer orderPrice;
        private Integer deliveryFee;
        private Integer totalPrice;
        private LocalDateTime createdAt;
        private List<OrderItemInfo> items;

        @Getter
        @AllArgsConstructor
        public static class OrderItemInfo {
            private String menuName;
            private Integer price;
            private Integer count;
        }
    }


}
