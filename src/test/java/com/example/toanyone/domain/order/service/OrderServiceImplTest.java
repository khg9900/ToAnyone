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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    //
    //  공통 Fixture 메서드
    //

    private User createUser(Long id, String email, String password, UserRole role) {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(user, "email", email);
        ReflectionTestUtils.setField(user, "password", password);
        ReflectionTestUtils.setField(user, "userRole", role);
        return user;
    }

    private Store createStore(User owner, Long id, Status status, int minOrderPrice, int deliveryFee) {
        Store store = new Store();
        ReflectionTestUtils.setField(store, "user", owner);
        ReflectionTestUtils.setField(store, "status", status);
        ReflectionTestUtils.setField(store, "openTime", LocalTime.of(9, 0));
        ReflectionTestUtils.setField(store, "closeTime", LocalTime.of(22, 0));
        ReflectionTestUtils.setField(store, "id", id);
        ReflectionTestUtils.setField(store, "minOrderPrice", minOrderPrice);
        ReflectionTestUtils.setField(store, "deliveryFee", deliveryFee);
        return store;
    }

    private Menu createMenu(Store store, Long id, String name, int price) {
        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "store", store);
        ReflectionTestUtils.setField(menu, "name", name);
        ReflectionTestUtils.setField(menu, "price", price);
        ReflectionTestUtils.setField(menu, "id", id);
        return menu;
    }

    private Cart createCart(User user, Store store, int totalPrice) {
        return new Cart(user, store, totalPrice);
    }

    private CartItem createCartItem(Cart cart, Menu menu, int quantity) {
        return new CartItem(cart, menu, quantity, menu.getPrice());
    }

    //
    //  테스트 시작
    //

    @Nested
    @DisplayName("주문 생성 (createOrder)")
    class CreateOrderTest {

        @Test
        @DisplayName("주문 생성 성공")
        void createOrderSuccess() {
            // given
            AuthUser authUser = new AuthUser(1L, "user@example.com", "USER");

            User user = createUser(1L, "user@example.com", "password", UserRole.USER);
            Store store = createStore(user, 100L, Status.OPEN, 5000, 3000);
            Menu menu = createMenu(store, 200L, "김치찌개", 10000);

            Cart cart = createCart(user, store, 0);
            CartItem cartItem = createCartItem(cart, menu, 2);
            cart.getCartItems().add(cartItem);

            ReflectionTestUtils.setField(cart, "totalPrice", cartItem.getMenu_price() * cartItem.getQuantity());

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(cartRepository.findByUserIdOrElseThrow(1L)).thenReturn(cart);
            when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
            when(menuRepository.findAllById(anyList()))
                    .thenReturn(cart.getCartItems().stream()
                            .map(CartItem::getMenu)
                            .toList());

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

    @Nested
    @DisplayName("주문 생성 실패")
    class CreateOrderFailTest {

        @Test
        @DisplayName("가게가 마감 상태라 주문 실패")
        void createOrderFailBecauseStoreClosed() {
            AuthUser authUser = new AuthUser(1L, "user@example.com", "USER");

            User user = createUser(1L, "user@example.com", "password", UserRole.USER);
            Store store = createStore(user, 100L, Status.CLOSED, 5000, 3000);
            Cart cart = createCart(user, store, 10000);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(cartRepository.findByUserIdOrElseThrow(1L)).thenReturn(cart);
            when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

            assertThatThrownBy(() -> orderService.createOrder(authUser))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining("가게가 영업 중이 아닙니다.");
        }

        @Test
        @DisplayName("최소 주문 금액을 만족하지 못해 주문 실패")
        void createOrderFailBecauseMinOrderPriceNotMet() {
            AuthUser authUser = new AuthUser(1L, "user@example.com", "USER");

            User user = createUser(1L, "user@example.com", "password", UserRole.USER);
            Store store = createStore(user, 100L, Status.OPEN, 20000, 3000); // 최소 주문 금액 20000원
            Cart cart = createCart(user, store, 10000); // 장바구니 총 금액 10000원

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(cartRepository.findByUserIdOrElseThrow(1L)).thenReturn(cart);
            when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

            assertThatThrownBy(() -> orderService.createOrder(authUser))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining("최소 주문 금액을 만족해야 합니다");
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 (updateOrderStatus)")
    class UpdateOrderStatusTest {

        @Test
        @DisplayName("주문 상태 변경 성공")
        void updateOrderStatusSuccess() {
            AuthUser authUser = new AuthUser(1L, "owner@example.com", "OWNER");

            User owner = createUser(1L, "owner@example.com", "password", UserRole.OWNER);
            Store store = createStore(owner, 100L, Status.OPEN, 5000, 3000);
            User customer = createUser(2L, "customer@example.com", "password", UserRole.USER);

            Order order = Order.builder()
                    .store(store)
                    .user(customer)
                    .status(OrderStatus.WAITING)
                    .totalPrice(15000)
                    .deliveryFee(3000)
                    .build();
            ReflectionTestUtils.setField(order, "id", 10L);

            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

            OrderDto.StatusUpdateRequest request = OrderDto.StatusUpdateRequest.builder()
                    .status(OrderStatus.COOKING.name())
                    .build();

            OrderDto.StatusUpdateResponse response = orderService.updateOrderStatus(authUser, order.getId(), request);

            assertThat(response.getOrderId()).isEqualTo(order.getId());
            assertThat(response.getUpdatedStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        @DisplayName("주문 상태 변경 실패 - 잘못된 요청 (변경할 수 없는 상태)")
        void updateOrderStatusInvalidTransition() {
            AuthUser authUser = new AuthUser(1L, "owner@example.com", "OWNER");

            User owner = createUser(1L, "owner@example.com", "password", UserRole.OWNER);
            Store store = createStore(owner, 100L, Status.OPEN, 5000, 3000);
            User customer = createUser(2L, "customer@example.com", "password", UserRole.USER);

            Order order = Order.builder()
                    .store(store)
                    .user(customer)
                    .status(OrderStatus.DELIVERING) // 이미 배달 중
                    .totalPrice(15000)
                    .deliveryFee(3000)
                    .build();
            ReflectionTestUtils.setField(order, "id", 10L);

            when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

            OrderDto.StatusUpdateRequest request = OrderDto.StatusUpdateRequest.builder()
                    .status(OrderStatus.COOKING.name()) // 잘못된 상태 이동
                    .build();

            assertThatThrownBy(() -> orderService.updateOrderStatus(authUser, order.getId(), request))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining("주문 상태 변경 순서가 올바르지 않습니다.");
        }
    }

    @Nested
    @DisplayName("내 주문 내역 조회 (getOrdersByUser)")
    class GetOrdersByUserTest {

        @Test
        @DisplayName("주문 내역 조회 성공")
        void getUserOrderHistorySuccess() {
            AuthUser authUser = new AuthUser(1L, "user@example.com", "USER");

            User user = createUser(1L, "user@example.com", "password", UserRole.USER);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            var result = orderService.getOrdersByUser(authUser);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("주문 내역 조회 실패 - 유저 없음")
        void getUserOrderHistoryFail_UserNotFound() {
            AuthUser authUser = new AuthUser(999L, "notfound@example.com", "USER");

            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.getOrdersByUser(authUser))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining("고객 정보가 없습니다.");
        }
    }

    @Nested
    @DisplayName("가게 주문 목록 조회 (getOrdersByStore)")
    class GetOrdersByStoreTest {

        @Test
        @DisplayName("가게 주문 목록 조회 성공")
        void getStoreOrdersSuccess() {
            AuthUser authUser = new AuthUser(1L, "owner@example.com", "OWNER");

            User owner = createUser(1L, "owner@example.com", "password", UserRole.OWNER);
            Store store = createStore(owner, 100L, Status.OPEN, 5000, 3000);

            when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(owner));
            when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

            var result = orderService.getOrdersByStore(authUser, store.getId());

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("가게 주문 목록 조회 실패 - 가게 없음")
        void getStoreOrdersFail_StoreNotFound() {
            AuthUser authUser = new AuthUser(1L, "owner@example.com", "OWNER");

            when(storeRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.getOrdersByStore(authUser, 999L))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining("고객 정보가 없습니다.");
        }
    }
}

