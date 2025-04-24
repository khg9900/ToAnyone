package com.example.toanyone.domain.cart.service;

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
import com.example.toanyone.global.common.dto.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public CartResponseDto createCart(AuthUser authUser, Long storeId ,Long menuId, Integer quantity) {

        User user = userRepository.findById(authUser.getId()).get();
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        if (!cartRepository.existsByUserId(authUser.getId())){
            Cart cart = new Cart(user, store, 0);
            cartRepository.save(cart);
        }

        Cart cart = cartRepository.findByUser(user);

        cart.setTotalPrice(menu.getPrice(), quantity);

        CartItem cartItem = new CartItem(cart, menu, quantity, menu.getPrice());
        cartItemRepository.save(cartItem);

        return new CartResponseDto("장바구니에 추가되었습니다");
    }
}
