package com.example.toanyone.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_log")
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime logTime; // 요청 시각 (직접 추가)

    private Long storeId;

    private Long orderId;

    private String action; // CREATE_ORDER, UPDATE_ORDER_STATUS 등

    public OrderLog(Long storeId, Long orderId, String action) {
        this.logTime = LocalDateTime.now();
        this.storeId = storeId;
        this.orderId = orderId;
        this.action = action;
    }
}
