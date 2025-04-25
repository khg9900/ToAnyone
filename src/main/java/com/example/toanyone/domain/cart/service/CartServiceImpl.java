package com.example.toanyone.domain.cart.service;

import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.domain.cart.dto.CartResponseDto;
import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.entity.CartItem;
import com.example.toanyone.domain.cart.repository.CartItemRepository;
import com.example.toanyone.domain.cart.repository.CartRepository;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartResponseDto createCart(AuthUser authUser, Long storeId , Long menuId, Integer quantity) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user, store, 0)));

        int price = menu.getPrice();

        cart.setTotalPrice(price, quantity);

        CartItem cartItem = new CartItem(cart, menu, quantity, price);
        cartItemRepository.save(cartItem);

        return new CartResponseDto("장바구니에 추가되었습니다");
    }

    @Override
    @Transactional
    public CartItemDto.Response getCartItems(AuthUser authUser) {
        Cart cart = cartRepository.findByUserIdOrElseThrow(authUser.getId());

        Long storeId = cart.getStore().getId();
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Integer orderPrice = cart.getTotalPrice(); //장바구니에 담긴 품목들의 총액

        Integer totalPrice = cart.getTotalPrice()+store.getDeliveryFee(); //배달비까지 더한 찐 결제 금액

        List<CartItemDto.CartItems> cartItems = cart.getCartItems().stream()
                .map(item -> new CartItemDto.CartItems(
                        item.getMenu().getId(),
                        item.getMenu().getName(),
                        item.getMenu().getPrice(),
                        item.getQuantity(),
                        item.getMenu_price() * item.getQuantity() //품목별 총액
                        )).collect(Collectors.toList());

        return new CartItemDto.Response(
                store.getName(), cartItems, orderPrice, store.getDeliveryFee(),totalPrice
        );
    }


    // 고승표 수정/추가
    @Override
    @Transactional
    public CartResponseDto clearCartItems(AuthUser authUser) {
<<<<<<< HEAD
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
        return clearCartItems(user);
    }
    // orderservice에서 user로 받을 수 있게
    @Override
    @Transactional
    public CartResponseDto clearCartItems(User user) {
        Cart cart = cartRepository.findByUserOrElseThrow(user);
=======

        Cart cart = cartRepository.findByUserIdOrElseThrow(authUser.getId());
>>>>>>> 53730a172b79bb4c6fa5a456629e74bcc1b42130
        cartRepository.delete(cart);
        return new CartResponseDto("장바구니가 비워졌습니다");
    }

    @Override
    @Transactional
    public CartResponseDto updateCartItems(AuthUser authUser, Long storeId, Long menuId, Integer quantity) {
        Cart cart = cartRepository.findByUserIdOrElseThrow(authUser.getId());
        CartItem cartItem = cartItemRepository.findByMenuIdAndCartOrElseThrow(menuId, cart);

        int changedQuantity = cartItem.getQuantity() + quantity;
        if (changedQuantity<0){
            throw new ApiException(ErrorStatus.CART_ITEM_QUANTITY_UNDERFLOW);
        }
        if (changedQuantity ==0) {
            cartItemRepository.delete(cartItem);
            cart.changeTotalPrice();
            return new CartResponseDto("해당 품목을 삭제합니다");
        }
        cartItem.setCartItemQuantity(changedQuantity);
        cart.changeTotalPrice();

        return new CartResponseDto("품목의 수량 변경이 완료되었습니다.");
    }


}
