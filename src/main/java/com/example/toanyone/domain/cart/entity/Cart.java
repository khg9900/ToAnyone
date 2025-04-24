package com.example.toanyone.domain.cart.entity;

import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

}
