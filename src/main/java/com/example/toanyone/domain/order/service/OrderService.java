package com.example.toanyone.domain.order.service;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.user.entity.User;
import java.util.List;


public interface OrderService {

    /*
      주문 생성 메서드

      @param user    현재 로그인한 사용자
      @param cartId  주문할 장바구니 ID
      @return 주문 생성 응답 DTO
    */
    List<OrderDto.OwnerOrderListResponse> getOrdersByStore(Long storeId);
    List<OrderDto.UserOrderHistoryResponse> getOrdersByUser(AuthUser authUser);
    OrderDto.CreateResponse createOrder(AuthUser authUser);

}
