package com.example.toanyone.domain.order.entity;

import com.example.toanyone.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDateTime logTime;

    @Lob
    private String logMessage;

    @PrePersist
    public void setLogTime() {
        this.logTime = LocalDateTime.now();
    }
}
