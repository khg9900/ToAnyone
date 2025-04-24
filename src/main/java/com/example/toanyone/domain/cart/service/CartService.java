package com.example.toanyone.domain.cart.service;

import com.example.toanyone.domain.cart.dto.CartResponseDto;

import com.example.toanyone.global.auth.dto.AuthUser;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    CartResponseDto createCart(AuthUser authUser, Long storeId, Long menuId, Integer quantity
    );
}