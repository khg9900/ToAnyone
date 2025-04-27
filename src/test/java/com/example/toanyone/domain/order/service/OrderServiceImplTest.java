package com.example.toanyone.domain.order.service;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.entity.CartItem;
import com.example.toanyone.domain.cart.repository.CartRepository;
import com.example.toanyone.domain.cart.service.CartService;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.entity.OrderItem;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.order.repository.OrderItemRepository;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("주문 생성 (createOrder)")
    class CreateOrderTest {

        @Test
        @DisplayName("주문 생성 성공")
        void createOrderSuccess() {
            // given
            AuthUser authUser = new AuthUser(1L, "user@example.com", "USER");

            User user = new User();
            ReflectionTestUtils.setField(user, "id", 1L);
            ReflectionTestUtils.setField(user, "email", "user@example.com");
            ReflectionTestUtils.setField(user, "password", "password");
            ReflectionTestUtils.setField(user, "userRole", UserRole.USER);

            Store store = new Store();
            ReflectionTestUtils.setField(store, "user", user);
            ReflectionTestUtils.setField(store, "status", Status.OPEN);
            ReflectionTestUtils.setField(store, "openTime", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(store, "closeTime", LocalTime.of(22, 0));
            ReflectionTestUtils.setField(store, "id", 100L); // storeId 세팅

            Menu menu = new Menu();
            ReflectionTestUtils.setField(menu, "store", store);
            ReflectionTestUtils.setField(menu, "name", "김치찌개");
            ReflectionTestUtils.setField(menu, "description", "맛있는 김치찌개");
            ReflectionTestUtils.setField(menu, "price", 10000);
            ReflectionTestUtils.setField(menu, "id", 200L); // menuId 세팅

            Cart cart = new Cart(user, store, 0);
            CartItem cartItem = new CartItem(cart, menu, 2, 10000);
            cart.getCartItems().add(cartItem);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(cartRepository.findByUserIdOrElseThrow(1L)).thenReturn(cart);
            when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
            when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            OrderDto.CreateResponse response = orderService.createOrder(authUser);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getStoreId()).isEqualTo(store.getId());
            assertThat(response.getStatus()).isEqualTo(OrderStatus.WAITING.name());
        }
    }
}
