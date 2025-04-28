package com.example.toanyone.domain.cart.service;

import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.global.auth.dto.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    void addCart(AuthUser authUser, Long storeId, Long menuId, Integer quantity);

    CartItemDto.Response getCartItems(AuthUser authUser);

    void clearCartItems(Long userId);

    void updateCartItems(AuthUser authUser, Long storeId, Long menuId, Integer quantity);
}