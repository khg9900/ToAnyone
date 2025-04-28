package com.example.toanyone.domain.cart.controller;


import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.domain.cart.dto.CartRequestDto;
import com.example.toanyone.domain.cart.service.CartService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    public final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addCart(
            @Auth AuthUser authUser, @Valid @RequestBody CartRequestDto cartRequestDto
    ) {
        cartService.addCart(authUser,cartRequestDto.getStoreId(), cartRequestDto.getMenuId(), cartRequestDto.getQuantity());
        return ApiResponse.onSuccess(SuccessStatus.CART_ITEM_ADDED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartItemDto.Response>> getCart(@Auth AuthUser authUser) {
        CartItemDto.Response response = cartService.getCartItems(authUser);
        return ApiResponse.onSuccess(SuccessStatus.CART_ITEM_FETCHED, response);
    }


    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(@Auth AuthUser authUser) {
        cartService.clearCartItems(authUser.getId());
        return ApiResponse.onSuccess(SuccessStatus.CART_ITEM_UPDATED);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Void>> updateCart(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody CartItemDto.CartItems cartItemDto
    ){
        cartService.updateCartItems(authUser, storeId, cartItemDto.getMenuId(), cartItemDto.getQuantity());
        return ApiResponse.onSuccess(SuccessStatus.CART_CLEARED);
    }

}
