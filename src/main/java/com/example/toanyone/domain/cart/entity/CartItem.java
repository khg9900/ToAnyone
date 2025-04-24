package com.example.toanyone.domain.cart.entity;

import com.example.toanyone.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Integer quantity;

    @Column(nullable = false)
    private Integer menu_price;

    public CartItem(Cart cart, Menu menu, Integer quantity, Integer menu_price) {
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
        this.menu_price = menu_price;
    }
}
