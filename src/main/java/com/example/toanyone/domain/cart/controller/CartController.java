package com.example.toanyone.domain.cart.controller;


import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.domain.cart.dto.CartRequestDto;
import com.example.toanyone.domain.cart.dto.CartResponseDto;
import com.example.toanyone.domain.cart.service.CartService;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.error.ApiException;
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
    public final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> createCart(
            @Auth AuthUser authUser, @Valid @RequestBody CartRequestDto cartRequestDto) {

        CartResponseDto cartResponseDto = cartService.createCart(
                authUser,cartRequestDto.getStoreId(), cartRequestDto.getMenuId(), cartRequestDto.getQuantity()
        );

        return ApiResponse.onSuccess(SuccessStatus.CREATED, cartResponseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartItemDto.Response>> getCart(
            @Auth AuthUser authUser){

        CartItemDto.Response response = cartService.getCartItems(authUser);

        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }


    @DeleteMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> clearCart(@Auth AuthUser authUser){
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
        CartResponseDto response = cartService.clearCartItems(user);

        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    public ResponseEntity<ApiResponse<CartResponseDto>> updateCart()

}
