package com.example.toanyone.domain.order.enums;

public enum OrderStatus {
    WAITING,       // 접수 대기
    BAN_ORDER,     // 주문 거절
    COOKING,       // 조리중
    DONE_COOKING,  // 조리완료
    DELIVERING,    // 배달중
    DELIVERED      // 배달완료
}
