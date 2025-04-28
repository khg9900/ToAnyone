package com.example.toanyone.domain.cart.service;

import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.domain.cart.dto.CartRequestDto;
import com.example.toanyone.domain.cart.dto.CartResponseDto;

import com.example.toanyone.domain.cart.entity.CartItem;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.auth.dto.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    CartResponseDto addCart(AuthUser authUser, Long storeId, CartRequestDto requestDto);

    CartItemDto.Response getCartItems(AuthUser authUser);

    CartResponseDto clearCartItems(Long userId);

    CartResponseDto updateCartItems(AuthUser authUser, Long storeId,
                                    Long menuId, Integer quantity);
}