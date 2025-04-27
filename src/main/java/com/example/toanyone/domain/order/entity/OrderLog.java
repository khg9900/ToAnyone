package com.example.toanyone.domain.order.entity;

import com.example.toanyone.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "order_log")
public class OrderLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    private Long orderId;

    private String action; // CREATE_ORDER, UPDATE_ORDER_STATUS 등

    public OrderLog(Long storeId, Long orderId, String action) {
        this.storeId = storeId;
        this.orderId = orderId;
        this.action = action;
    }
}
