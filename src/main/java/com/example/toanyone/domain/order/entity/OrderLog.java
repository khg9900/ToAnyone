package com.example.toanyone.domain.order.entity;

import com.example.toanyone.domain.order.aop.OrderLogAspect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_log")
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    private Long orderId;

    private String status; // CREATE_ORDER, UPDATE_ORDER_STATUS 등

    private LocalDateTime logTime; // 요청 시각 (직접 추가)

    public OrderLog(Long storeId, Long orderId, String status, LocalDateTime logTime) {
        this.storeId = storeId;
        this.orderId = orderId;
        this.status = status;
        this.logTime = logTime;
    }
}
