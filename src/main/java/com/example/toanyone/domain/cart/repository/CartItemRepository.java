package com.example.toanyone.domain.cart.repository;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.entity.CartItem;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.menu.id = :menuId AND c.cart = :cart")
    Optional<CartItem> findByMenuIdAndCart(@Param("menuId") Long menuId, @Param("cart") Cart cart);

    default CartItem findByMenuIdAndCartOrElseThrow(Long menuId, Cart cart) {
        return findByMenuIdAndCart(menuId, cart).orElseThrow(
                ()-> new ApiException(ErrorStatus.CART_ITEMS_NOT_FOUND));
    }

}
