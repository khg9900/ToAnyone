package com.example.toanyone.domain.cart.controller;


import com.example.toanyone.domain.cart.dto.CartRequestDto;
import com.example.toanyone.domain.cart.dto.CartResponseDto;
import com.example.toanyone.domain.cart.service.CartService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    public final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(
            @Auth AuthUser authUser, @RequestBody CartRequestDto cartRequestDto) {

        CartResponseDto cartResponseDto = cartService.createCart(
                authUser ,cartRequestDto.getStoreId(), cartRequestDto.getMenuId(), cartRequestDto.getQuantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDto);
    }

}
