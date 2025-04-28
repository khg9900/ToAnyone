package com.example.toanyone.domain.cart.service;
import com.example.toanyone.domain.cart.dto.CartItemDto;
import com.example.toanyone.domain.cart.dto.CartRequestDto;
import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.entity.CartItem;
import com.example.toanyone.domain.cart.repository.CartItemRepository;
import com.example.toanyone.domain.cart.repository.CartRepository;
import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;

    @Test
    void 메뉴_추가를_성공(){
        // given
        Long storeId = 1L;
        Long menuId = 1L;
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "kkk@gmail.com", "OWNER");

        Store store = mock(Store.class);
        Menu menu = new Menu(store, new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK"));
        ReflectionTestUtils.setField(menu, "id", menuId);

        User user = mock(User.class);
        Cart cart = new Cart(user, store, 10000); // 초기 총액 10000

        given(cartRepository.existsByUserId(userId)).willReturn(true);
        given(cartRepository.findByUserIdOrElseThrow(userId)).willReturn(cart);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);
        given(menuRepository.existsByIdAndStoreId(menuId, storeId)).willReturn(true);

        // when
        cartService.addCart(authUser, storeId, menuId, 2); // 메뉴 2개 추가

        // then
        verify(cartRepository).save(cart);
        verify(cartItemRepository).save(any(CartItem.class));
        assertEquals(12000, cart.getTotalPrice()); // (10000 + 1000*2 = 12000)
    }

    @Test
    void 장바구니_생성과_메뉴_추가를_성공() {
        // given
        Long storeId = 1L;
        Long menuId = 1L;
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "kkk@gmail.com", "OWNER");

        Store store = mock(Store.class);
        Menu menu = new Menu(store, new MenuDto.Request("originMenu", "description", 1000, "KOREAN", "DRINK"));
        ReflectionTestUtils.setField(menu, "id", menuId);

        User user = mock(User.class);
        Cart cart = new Cart(user, store, 0);  // Cart 객체 생성

        // Mocking repository behavior
        given(cartRepository.existsByUserId(userId)).willReturn(false); // 첫 번째 생성이므로 false 반환
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);
        given(menuRepository.findByIdOrElseThrow(menuId)).willReturn(menu);
        given(menuRepository.existsByIdAndStoreId(menuId, storeId)).willReturn(true);

        // when
        cartService.addCart(authUser, storeId, menuId, 2); // 메뉴 2개 추가
/*
  ArgumentCaptor : 특정 메서드가 호출될 때 전달된 인자를 캡처 → 나중에 해당 인자에 대해 검증하거나 테스트할 수 있게 해줌

 */
        // then
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);  // Cart 객체 캡처
        verify(cartRepository).save(cartCaptor.capture());  // cartRepository.save() 호출 시 캡처

        // 캡처된 Cart 객체를 검증
        Cart capturedCart = cartCaptor.getValue();
        assertNotNull(capturedCart);
        assertEquals(cart.getUser(), capturedCart.getUser());  // 사용자 정보가 동일한지 확인
        assertEquals(cart.getStore(), capturedCart.getStore());  // 가게 정보가 동일한지 확인
        assertEquals(2000, capturedCart.getTotalPrice());  // 총 가격이 올바르게 계산되었는지 확인

        verify(cartItemRepository).save(any(CartItem.class));  // CartItem이 저장되는지 확인
    }


    @Test
    void 가게_폐업으로_인한_메뉴_추가_실패() {
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        given(storeRepository.getDeletedById(storeId)).willReturn(true);

        ApiException exception = assertThrows(ApiException.class, () ->
                cartService.addCart(authUser, storeId, menuId, 2)
        );

        assertEquals(ErrorStatus.STORE_SHUT_DOWN, exception.getErrorCode());
    }

    @Test
    void 메뉴_삭제로_인한_메뉴_추가_실패() {
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        given(storeRepository.getDeletedById(storeId)).willReturn(false);
        given(menuRepository.getDeletedById(menuId)).willReturn(true);

        ApiException exception = assertThrows(ApiException.class, () ->
                cartService.addCart(authUser, storeId, menuId, 2)
        );

        assertEquals(ErrorStatus.MENU_ALREADY_DELETED, exception.getErrorCode());
    }

    @Test
    void 메뉴가_가게에_존재하지않아_메뉴_추가_실패() {
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        given(storeRepository.getDeletedById(storeId)).willReturn(false);
        given(menuRepository.getDeletedById(menuId)).willReturn(false);
        given(menuRepository.existsByIdAndStoreId(menuId, storeId)).willReturn(false);

        ApiException exception = assertThrows(ApiException.class, () ->
                cartService.addCart(authUser, storeId, menuId, 2)
        );

        assertEquals(ErrorStatus.MENU_IS_NOT_IN_STORE, exception.getErrorCode());
    }

    @Test
    void 유저를_찾을수없어_메뉴_추가_실패() {
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "kkk@gmail.com", "OWNER");

        given(storeRepository.getDeletedById(storeId)).willReturn(false);
        given(menuRepository.getDeletedById(menuId)).willReturn(false);
        given(menuRepository.existsByIdAndStoreId(menuId, storeId)).willReturn(true);
        given(cartRepository.existsByUserId(authUser.getId())).willReturn(false);
        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                cartService.addCart(authUser, storeId, menuId, 2)
        );

        assertEquals(ErrorStatus.USER_NOT_FOUND, exception.getErrorCode());
    }


    /*
    2. 장바구니에 들어있는 메뉴들 가져오기
     */
    @Test
    void getCartItems_success() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(userId, "test@email.com", "USER");

        Store store = mock(Store.class);
        given(store.getId()).willReturn(storeId);
        given(store.getName()).willReturn("테스트가게");
        given(store.getDeliveryFee()).willReturn(3000);

        Menu menu = mock(Menu.class);
        given(menu.getId()).willReturn(menuId);
        given(menu.getName()).willReturn("테스트메뉴");
        given(menu.getPrice()).willReturn(5000);

        CartItem cartItem = mock(CartItem.class);
        given(cartItem.getMenu()).willReturn(menu);
        given(cartItem.getQuantity()).willReturn(2);
        given(cartItem.getMenu_price()).willReturn(5000);

        Cart cart = mock(Cart.class);
        given(cart.getStore()).willReturn(store);
        given(cart.getTotalPrice()).willReturn(10000);
        given(cart.getCartItems()).willReturn(List.of(cartItem));

        given(cartRepository.findByUserIdOrElseThrow(userId)).willReturn(cart);
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store);

        // when
        CartItemDto.Response response = cartService.getCartItems(authUser);

        // then
        assertEquals("테스트가게", response.getStoreName());
        assertEquals(1, response.getItems().size());

        CartItemDto.CartItems item = response.getItems().get(0);
        assertEquals(menuId, item.getMenuId());
        assertEquals("테스트메뉴", item.getMenuName());
        assertEquals(5000, item.getMenuPrice());
        assertEquals(2, item.getQuantity());
        assertEquals(10000, response.getOrderPrice()); // 메뉴 가격 * 수량 (배달비 빠진거)
        assertEquals(3000, response.getDeliveryFee());
        assertEquals(13000, response.getTotalPrice()); // 총액 + 배달비
    }



    /*
    3. 장바구니 비우기
     */

    @Test
    void clearCartItems_success() {
        Long userId = 1L;
        Cart cart = new Cart();

        given(cartRepository.findByUserIdOrElseThrow(userId)).willReturn(cart);

        cartService.clearCartItems(userId);

        verify(cartRepository).delete(cart);
    }

    /*
    4. 장바구니 수량 변경하기
     */

    @Test
    void 수량_변경_성공() {
        AuthUser authUser = new AuthUser(1L, "user@email.com", "USER");
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();

        ReflectionTestUtils.setField(cartItem, "quantity", 2);
        ReflectionTestUtils.setField(cartItem, "menu_price", 1000);
        cart.getCartItems().add(cartItem);

        given(cartRepository.findByUserIdOrElseThrow(authUser.getId())).willReturn(cart);
        given(cartItemRepository.findByMenuIdAndCartOrElseThrow(1L, cart)).willReturn(cartItem);

        cartService.updateCartItems(authUser, 1L, 1L, 1); // 수량 +1

        assertEquals(3, cartItem.getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void 들어있는_수량보다_더_많이_줄인다() {
        AuthUser authUser = new AuthUser(1L, "user@email.com", "USER");
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();

        ReflectionTestUtils.setField(cartItem, "quantity", 0);
        cart.getCartItems().add(cartItem);

        given(cartRepository.findByUserIdOrElseThrow(authUser.getId())).willReturn(cart);
        given(cartItemRepository.findByMenuIdAndCartOrElseThrow(1L, cart)).willReturn(cartItem);

        ApiException exception = assertThrows(ApiException.class, () ->
                cartService.updateCartItems(authUser, 1L, 1L, -1) // 수량 -1 => 음수
        );

        assertEquals(ErrorStatus.CART_ITEM_QUANTITY_UNDERFLOW, exception.getErrorCode());
    }

    @Test
    void 수량이_0이_되면_삭제() {
        AuthUser authUser = new AuthUser(1L, "user@email.com", "USER");
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();

        ReflectionTestUtils.setField(cartItem, "quantity", 1);
        ReflectionTestUtils.setField(cartItem, "menu_price", 1000);
        cart.getCartItems().add(cartItem);


        given(cartRepository.findByUserIdOrElseThrow(authUser.getId())).willReturn(cart);
        given(cartItemRepository.findByMenuIdAndCartOrElseThrow(1L, cart)).willReturn(cartItem);

        cartService.updateCartItems(authUser, 1L, 1L, -1); // 수량 -1 => 0

        verify(cartItemRepository).delete(cartItem);
        verify(cartRepository, times(2)).save(cart); // cartItem 삭제 후 cart 총액 갱신 저장
    }
}


