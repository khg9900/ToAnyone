//package com.example.toanyone.domain.order.service;
//
//import com.example.toanyone.domain.cart.entity.Cart;
//import com.example.toanyone.domain.cart.service.CartService;
//import com.example.toanyone.domain.order.dto.OrderDto;
//import com.example.toanyone.domain.order.entity.Order;
//import com.example.toanyone.domain.order.entity.OrderItem;
//import com.example.toanyone.domain.order.enums.OrderStatus;
//import com.example.toanyone.domain.order.repository.OrderItemRepository;
//import com.example.toanyone.domain.order.repository.OrderRepository;
//import com.example.toanyone.domain.store.entity.Store;
//import com.example.toanyone.domain.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
///*
//  주문 관련 비즈니스 로직을 구현한 서비스 클래스
//  - 주문 생성
//  - 주문 항목(OrderItem) 저장
//  - 장바구니 초기화 등
//*/
//
//@Service
//@RequiredArgsConstructor
//public class OrderServiceImpl implements OrderService {
//
//    // 의존성 주입 (final + RequiredArgsConstructor 덕분에 자동 주입됨)
//    private final OrderRepository orderRepository;
//    private final OrderItemRepository orderItemRepository;
//    private final CartService cartService;
//
//    /*
//     주문 생성 메서드
//     - 고객이 장바구니(cartId)를 기반으로 주문을 생성
//     - 유효성 검사: 장바구니 존재, 가게 상태, 최소 주문 금액 확인
//     - 주문(Order)과 주문 항목(OrderItem) 저장
//     - 장바구니 초기화
//
//     @param user 현재 로그인한 사용자 (주문자)
//     @param cartId 주문하고자 하는 장바구니 ID
//     @return 주문 생성 결과 DTO
//    */
//    @Transactional
//    @Override
//    public OrderDto.CreateResponse createOrder(User user, Long cartId) {
//        // 1. 장바구니 조회
//        Cart cart = cartService.getCartById(cartId);
//        if (cart == null) {
//            throw new IllegalArgumentException("장바구니를 찾을 수 없습니다.");
//        }
//
//        // 2. 장바구니와 연결된 가게 정보 가져오기
//        Store store = cart.getStore();
//
//        // 3. 가게가 영업 중인지 확인 (CLOSED, TEMP_CLOSED이면 주문 불가)
//        if (!store.isOpen()) {
//            throw new IllegalStateException("[" + store.getName() + "] 가게는 현재 영업 중이 아닙니다.");
//        }
//
//        // 4. 메뉴 총 금액 계산 (배달비는 제외)
//        int orderPrice = cart.calculateTotalPrice();
//
//        // 5. 최소 주문 금액을 충족하는지 확인
//        if (orderPrice < store.getMinOrderPrice()) {
//            throw new IllegalArgumentException("최소 주문 금액을 만족해야 합니다.");
//        }
//
//        // 6. 주문 엔티티 생성 및 저장 (상태는 기본값 WAITING)
//        Order order = Order.builder()
//                .store(store) // 어떤 가게의 주문인지
//                .user(user) // 누가 주문했는지
//                .status(OrderStatus.WAITING) // 접수 대기 상태
//                .totalPrice(orderPrice + store.getDefaultDeliveryFee()) // 총 금액 = 메뉴 + 배달비
//                .defaultDeliveryFee(store.getDefaultDeliveryFee()) // 배달비 저장
//                .build();
//
//        orderRepository.save(order); // 주문 저장
//
//        // 7. 장바구니 내 메뉴들을 주문 항목(OrderItem)으로 변환 후 저장
//        List<OrderItem> orderItems = cart.toOrderItems(order); // 장바구니에서 주문 항목 리스트 생성
//        for (OrderItem item : orderItems) {
//            orderItemRepository.save(item); // 하나씩 저장
//        }
//
//        // 8. 장바구니 비우기 (주문 완료 시 초기화)
//        cartService.clearCart(cart);
//
//        // 9. 생성된 주문 정보를 응답 DTO로 변환해서 반환
//        return new OrderDto.CreateResponse(
//                order.getId(), // 주문 ID
//                order.getCreatedAt(), // 생성 시각
//                order.getStatus().name() // 주문 상태 문자열
//        );
//    }
//}
