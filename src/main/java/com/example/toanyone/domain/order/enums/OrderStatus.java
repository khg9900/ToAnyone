package com.example.toanyone.domain.order.enums;

public enum OrderStatus {
    WAITING,       // 접수 대기
    BAN_ORDER,     // 주문 거절
    COOKING,       // 조리중
    DONE_COOKING,  // 조리완료
    DELIVERING,    // 배달중
    DELIVERED;      // 배달완료

    // 상태 변경 허용 여부 체크 메서드 추가
    public boolean isValidNextStatus(OrderStatus nextStatus) {
        switch (this) {
            case WAITING -> {
                return nextStatus == BAN_ORDER || nextStatus == COOKING;
            }
            case COOKING -> {
                return nextStatus == DONE_COOKING;
            }
            case DONE_COOKING -> {
                return nextStatus == DELIVERING;
            }
            case DELIVERING -> {
                return nextStatus == DELIVERED;
            }
            default -> {
                return false; // BAN_ORDER, DELIVERED는 추가 변경 불가
            }
        }
    }
}
