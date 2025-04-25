package com.example.toanyone.domain.cart.repository;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.entity.CartItem;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemsByMenuAndCart(Menu menu, Cart cart);

    default CartItem findCartItemsByMenuAndCartOrElseThrow(Menu menu, Cart cart) {
        return findCartItemsByMenuAndCart(menu, cart).orElseThrow(
                ()-> new ApiException(ErrorStatus.CART_ITEMS_NOT_FOUND));
    }

}
