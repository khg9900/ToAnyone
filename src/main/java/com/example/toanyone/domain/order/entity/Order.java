package com.example.toanyone.domain.order.entity;

import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer totalPrice;

    @Column(nullable = false)
    private Integer deliveryFee;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 이윤승
     * 리뷰와 오더 1:1 연관관계 매핑
     * */
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Review review;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }
}
