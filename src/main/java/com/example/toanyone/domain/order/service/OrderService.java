package com.example.toanyone.domain.order.service;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.user.entity.User;
import java.util.List;


public interface OrderService {


    // 주문 생성
    OrderDto.CreateResponse createOrder(AuthUser authUser);

    // 사장님 - 가게 주문 목록 조회
    List<OrderDto.OwnerOrderListResponse> getOrdersByStore(AuthUser authUser, Long storeId);

    // 유저 - 내 주문 내역 조회
    List<OrderDto.UserOrderHistoryResponse> getOrdersByUser(AuthUser authUser);

    // 사장님 - 주문 상태 변경
    OrderDto.StatusUpdateResponse updateOrderStatus(AuthUser authUser, Long orderId, OrderDto.StatusUpdateRequest request);


}
