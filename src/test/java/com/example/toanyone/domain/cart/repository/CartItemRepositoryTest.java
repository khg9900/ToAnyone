package com.example.toanyone.domain.cart.repository;


import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.entity.CartItem;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;

import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
/*
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 카트아이템_정상_조회_성공() {
        // given
        User user = userRepository.save(new User("test@email.com", "1234Abc!", "테스트유저", UserRole.USER,
                "nickname", "010-0000-0000", "address", "MALE", "2000-01-01"));

        Store store = storeRepository.save(new Store(user, new StoreRequestDto.Create(
                "테스트가게", "서울시", "10:00", "22:00", 1000, 2000, "공지사항", "OPEN", "000-0000-0000")));

        Menu menu = menuRepository.save(new Menu(store, new MenuDto.Request(
                "테스트메뉴", "맛있는 메뉴", 5000, "KOREAN", "FOOD")));

        Cart cart = cartRepository.save(new Cart(user, store, 0));

        CartItem cartItem = cartItemRepository.save(new CartItem(cart, menu, 2, menu.getPrice()));

        // when
        CartItem foundCartItem = cartItemRepository.findByMenuIdAndCartOrElseThrow(menu.getId(), cart);

        // then
        assertNotNull(foundCartItem);
        assertEquals(cartItem.getId(), foundCartItem.getId());
        assertEquals(2, foundCartItem.getQuantity());
    }
}
*/