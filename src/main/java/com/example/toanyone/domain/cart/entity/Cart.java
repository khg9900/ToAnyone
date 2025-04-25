package com.example.toanyone.domain.cart.entity;

import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;
import jakarta.persistence.*;
import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.entity.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Integer totalPrice;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();


    public Cart(User user, Store store, Integer totalPrice){
        this.user = user;
        this.store = store;
        this.totalPrice = totalPrice;
    }

    public void setTotalPrice(Integer price, Integer quantity) {
        this.totalPrice += price * quantity;
    }

    public void changeTotalPrice() {
        int changedPrice = 0;
        for (CartItem cartItem : cartItems) {
            changedPrice += cartItem.getMenu_price()*cartItem.getQuantity();
        }
        this.totalPrice = changedPrice;
    }

    // 고승표 추가
    // 장바구 담긴 항목 -> 주문 항목 생성
    public List<OrderItem> toOrderItems(Order order) {
        return this.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .menu(cartItem.getMenu())
                        .quantity(cartItem.getQuantity())
                        .menuPrice(cartItem.getMenu_price())
                        .build())
                .collect(Collectors.toList());
    }

}
