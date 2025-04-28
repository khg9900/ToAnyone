package com.example.toanyone.domain.cart.service;

import com.example.toanyone.domain.cart.dto.CartItemDto;
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
    public void addCart(AuthUser authUser, Long storeId , Long menuId, Integer quantity) {

        // 가게 폐업 여부 판별
        if (storeRepository.getDeletedById(storeId)) {
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }

        // 메뉴 삭제 여부 판별
        if (menuRepository.getDeletedById(menuId)) {
            throw new ApiException(ErrorStatus.MENU_ALREADY_DELETED);
        }

        // 선택한 가게에 해당하는 메뉴가 있는지 확인
        if (!menuRepository.existsByIdAndStoreId(menuId, storeId)) {
            throw new ApiException(ErrorStatus.MENU_IS_NOT_IN_STORE);
        }

        Cart cart;

        // 첫 번째 생성일 경우
        if (!cartRepository.existsByUserId(authUser.getId())) {

            User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
            Store store = storeRepository.findByIdOrElseThrow(storeId);

            cart = new Cart(user, store, 0);
        } else {
            // 두 번째 이후
            cart = cartRepository.findByUserIdOrElseThrow(authUser.getId());
        }

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        cart.setTotalPrice(menu.getPrice(), quantity);

        CartItem cartItem = new CartItem(cart, menu, quantity, menu.getPrice());

        cartRepository.save(cart);
        cartItemRepository.save(cartItem);
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

    // orderservice에서 user로 받을 수 있게
    @Override
    @Transactional
    public void clearCartItems(Long userId) {
        Cart cart = cartRepository.findByUserIdOrElseThrow(userId);
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void updateCartItems(AuthUser authUser, Long storeId, Long menuId, Integer quantity) {
        Cart cart = cartRepository.findByUserIdOrElseThrow(authUser.getId());
        CartItem cartItem = cartItemRepository.findByMenuIdAndCartOrElseThrow(menuId, cart);

        int changedQuantity = cartItem.getQuantity() + quantity;
        if (changedQuantity<0){
            throw new ApiException(ErrorStatus.CART_ITEM_QUANTITY_UNDERFLOW);
        }
        if (changedQuantity == 0) {
            cartItemRepository.delete(cartItem);
            cart.changeTotalPrice();
            cartRepository.save(cart);
        }
        cartItem.setCartItemQuantity(changedQuantity);
        cart.changeTotalPrice();
        cartRepository.save(cart);
    }


}
