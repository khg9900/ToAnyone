package com.example.toanyone.domain.order.service;

import com.example.toanyone.domain.cart.entity.Cart;
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
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    /*
         주문 생성 메서드
         - 고객의 Auth 정보를 기반으로 주문을 생성
         - 유효성 검사: 장바구니 존재, 가게 상태, 최소 주문 금액 확인
         - 주문(Order)과 주문 항목(OrderItem) 저장
         - 장바구니 초기화

         @param authUser 현재 로그인한 사용자
         @return 주문 생성 결과 DTO
    */

    @Transactional
    @Override
    public OrderDto.CreateResponse createOrder(AuthUser authUser) {
        // 0. 유저 정보 조회
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        // 1. 장바구니 조회
        Cart cart = cartRepository.findByUserIdOrElseThrow(user.getId());
        if (cart == null) {
            throw new ApiException(ErrorStatus.CART_NOT_FOUND);
        }

        // 2. 장바구니와 연결된 가게 정보 가져오기
        Long storeId = cart.getStore().getId();
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ErrorStatus.STORE_NOT_FOUND));

        // 3. 가게가 영업 중인지 확인 (CLOSED, TEMP_CLOSED이면 주문 불가)
        if (!store.isOpen()) {
            throw new ApiException(ErrorStatus.ORDER_STORE_CLOSED);
        }

        // 4. 메뉴 총 금액 계산 (배달비 제외)
        int orderPrice = cart.getTotalPrice();

        // 5. 최소 주문 금액을 충족하는지 확인
        if (orderPrice < store.getMinOrderPrice()) {
            throw new ApiException(ErrorStatus.ORDER_MIN_PRICE_NOT_MET);
        }

        // 6. 주문 엔티티 생성 및 저장 (상태는 기본값 WAITING)
        Order order = Order.builder()
                .store(store) // 어떤 가게의 주문인지
                .user(user) // 누가 주문했는지
                .status(OrderStatus.WAITING) // 접수 대기 상태
                .totalPrice(orderPrice + store.getDeliveryFee()) // 총 금액 = 메뉴 + 배달비
                .deliveryFee(store.getDeliveryFee()) // 배달비 저장
                .build();

        orderRepository.save(order); // 주문 저장

        // 7. 장바구니 내 메뉴들을 주문 항목(OrderItem)으로 변환 후 저장(리펙토링 완료)

        // 1. 장바구니 메뉴 ID 리스트 추출
        List<Long> menuIds = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getMenu().getId())
                .toList();

        // 2. 메뉴를 한 번에 조회
        List<Menu> menus = menuRepository.findAllById(menuIds);

        // 3. 메뉴를 Map으로 변환 (menuId -> Menu 객체)
        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        // 4. 주문 항목(OrderItem) 생성
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Menu menu = menuMap.get(cartItem.getMenu().getId());
                    if (menu == null) {
                        throw new ApiException(ErrorStatus.MENU_NOT_FOUND);
                    }
                    return OrderItem.builder()
                            .order(order)
                            .menu(menu)
                            .quantity(cartItem.getQuantity())
                            .menuPrice(cartItem.getMenu_price())
                            .build();
                })
                .toList();

        // 5. 주문 항목을 한 번에 저장
        orderItemRepository.saveAll(orderItems);

        // 8. 장바구니 비우기 (주문 완료 시 초기화)
        cartService.clearCartItems(user.getId());

        // 9. 생성된 주문 정보를 응답 DTO로 변환해서 반환
        return new OrderDto.CreateResponse(
                order.getStore().getId(),   // storeId (가게 ID)
                order.getId(),              // orderId (주문 ID)
                order.getCreatedAt(),       // createdAt (생성 시간)
                order.getStatus().name()    // status (주문 상태)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto.OwnerOrderListResponse> getOrdersByStore(AuthUser authUser, Long storeId) {
        // 1. 유저 정보 가져오기
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        // 2. 가게 조회 + 사장님 본인인지 검증
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ErrorStatus.STORE_NOT_FOUND));
        if (!store.getUser().getId().equals(user.getId())) {
            throw new ApiException(ErrorStatus.ORDER_ACCESS_DENIED_BY_NON_OWNER); //  사장님 아님
        }

        // 3. 해당 가게 주문 목록 조회
        List<Order> orders = orderRepository.findAllByStoreId(storeId);

        return orders.stream()
                .map(order -> new OrderDto.OwnerOrderListResponse(
                        order.getId(),
                        order.getUser().getNickname(),
                        order.getStatus().name(),
                        order.getCreatedAt()
                ))
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDto.UserOrderHistoryResponse> getOrdersByUser(AuthUser authUser) {
        // 1. 유저 정보 가져오기 (nickname 포함)
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        // 2. 주문 목록 조회
        List<Order> orders = orderRepository.findAllByUser(user);

        // 3. 주문 목록을 DTO로 변환
        return orders.stream()
                .map(order -> {
                    List<OrderDto.UserOrderHistoryResponse.OrderItemInfo> items = order.getOrderItems().stream()
                            .map(item -> new OrderDto.UserOrderHistoryResponse.OrderItemInfo(
                                    item.getMenu().getName(),
                                    item.getMenuPrice(),
                                    item.getQuantity()
                            ))
                            .toList();

                    return new OrderDto.UserOrderHistoryResponse(
                            order.getId(),
                            order.getStore().getName(),
                            order.getStatus().name(),
                            order.getTotalPrice() - order.getDeliveryFee(),
                            order.getDeliveryFee(),
                            order.getTotalPrice(),
                            order.getCreatedAt(),
                            items
                    );
                })
                .toList();
    }

    // 사장님 - 주문 상태 변경
    @Override
    @Transactional
    public OrderDto.StatusUpdateResponse updateOrderStatus(AuthUser authUser, Long orderId, OrderDto.StatusUpdateRequest request) {
        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorStatus.ORDER_NOT_FOUND));

        // 2. 주문에 연결된 가게 정보 가져오기
        Store store = order.getStore();

        // 3. 로그인한 사장님과 가게 주인이 일치하는지 검증
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus.ORDER_ACCESS_DENIED_BY_NON_OWNER);
        }

        // 4. 새로운 주문 상태로 업데이트
        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());

        if (!order.getStatus().isValidNextStatus(newStatus)) {
            throw new ApiException(ErrorStatus.ORDER_INVALID_STATUS_CHANGE);
        }

        order.changeStatus(newStatus);

        // ✨ 여기서 상태변경 결과 DTO 생성해서 return
        return OrderDto.StatusUpdateResponse.builder()
                .storeId(order.getStore().getId())
                .orderId(order.getId())
                .updatedStatus(order.getStatus().name())
                .build();
    }

}
