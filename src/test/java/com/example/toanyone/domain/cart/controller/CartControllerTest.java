package com.example.toanyone.domain.cart.controller;
import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.domain.cart.dto.CartRequestDto;
import com.example.toanyone.domain.cart.service.CartService;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
    @InjectMocks
    private CartController cartController;
    @Mock
    private CartService cartService;

    @Test
    void 장바구니에_아이템을_추가한다() {
        // given
        Long storeId = 1L;
        Long menuId = 1L;
        int quantity = 2;
        AuthUser authUser = new AuthUser(1L, "test@example.com", "USER");
        CartRequestDto cartRequestDto = new CartRequestDto(storeId, menuId, quantity);


        // when
        ResponseEntity<ApiResponse<Void>> response = cartController.addCart(authUser, cartRequestDto);

        // then
        assertEquals("201 CREATED", response.getStatusCode().toString());
        verify(cartService).addCart(authUser, storeId, menuId, quantity);
    }

    @Test
    void 장바구니를_가져온다(){
        AuthUser authUser = new AuthUser(1L, "test@example.com", "USER");

        // when
        ResponseEntity<ApiResponse<CartItemDto.Response>> response = cartController.getCart(authUser);

        //then
        assertEquals("200 OK", response.getStatusCode().toString());
        verify(cartService).getCartItems(authUser);
    }

    @Test
    void 장바구니를_비운다(){
        AuthUser authUser = new AuthUser(1L, "test@example.com", "USER");

        // when
        ResponseEntity<ApiResponse<Void>> response = cartController.clearCart(authUser);

        //then
        assertEquals("204 NO_CONTENT", response.getStatusCode().toString());
        verify(cartService).clearCartItems(authUser.getId());
    }

    @Test
    void 장바구니의_메뉴_수량을_바꾼다(){
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "test@example.com", "USER");
        CartItemDto.CartItems cartItemDto = new CartItemDto.CartItems(menuId, "name", 1000, 1, 10000);

        // when
        ResponseEntity<ApiResponse<Void>> response = cartController.updateCart(authUser, storeId, cartItemDto);

        //then
        assertEquals("204 NO_CONTENT", response.getStatusCode().toString());
        verify(cartService).updateCartItems(authUser, storeId, cartItemDto.getMenuId(),
                cartItemDto.getQuantity());
    }

}
