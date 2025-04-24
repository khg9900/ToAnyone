package com.example.toanyone.domain.order.service;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.cart.service.CartService;
import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.entity.OrderItem;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.order.repository.OrderItemRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
  주문 관련 비즈니스 로직을 처리하는 서비스 클래스
*/

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;       // 주문 저장소
    private final OrderItemRepository orderItemRepository; // 주문 항목 저장소
    private final CartService cartService;               // 장바구니 서비스

    /*
      주문 생성 메서드
      - 유저가 장바구니(cartId)를 주문할 때 호출됨
      - 가게 오픈 상태, 최소 주문 금액 조건 확인
      - 주문 및 주문 항목 저장
      - 장바구니 비우기

      @param user 현재 로그인한 사용자
      @param cartId 주문할 장바구니 ID
      @return 주문 생성 응답 DTO
    */
    @Transactional
    public OrderDto.CreateResponse createOrder(User user, Long cartId) {

        // 1. 장바구니 조회
        Cart cart = cartService.getCartById(cartId);

        // 2. 장바구니에 연결된 가게 정보 가져오기
        Store store = cart.getStore();

        // 3. 가게 오픈 여부 확인 (CLOSED, TEMP_CLOSED 등 방지)
        if (!store.isOpen()) {
            throw new IllegalStateException("가게가 현재 영업 중이 아닙니다.");
        }

        // 4. 최소 주문 금액 확인 (배달비 제외)
        int orderPrice = cart.calculateTotalPrice(); // 메뉴 총 금액 계산
        if (orderPrice < store.getMinOrderPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 만족해야 합니다.");
        }

        // 5. 주문 객체 생성 및 저장
        Order order = Order.builder()
                .store(store)
                .user(user)
                .status(OrderStatus.WAITING) // 기본 상태: 접수 대기
                .totalPrice(orderPrice + store.getDefaultDeliveryFee())
                .defaultDeliveryFee(store.getDefaultDeliveryFee())
                .build();

        orderRepository.save(order); // 주문 저장

        // 6. 장바구니 안 메뉴들을 주문 항목으로 변환 및 저장
        List<OrderItem> orderItems = cart.toOrderItems(order);
        for (OrderItem item : orderItems) {
            orderItemRepository.save(item);
        }

        // 7. 장바구니 초기화
        cartService.clearCart(cart);

        // 8. 응답 DTO 생성 및 반환
        return new OrderDto.CreateResponse(
                order.getId(),
                order.getCreatedAt( ),
                order.getStatus().name()
        );
    }
}
