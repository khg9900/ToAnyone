package com.example.toanyone.domain.order.controller;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.service.OrderService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.response.ApiResponse;
import com.example.toanyone.global.common.code.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderDto.CreateResponse>> createOrder(
            @Auth AuthUser authUser
    ) {
        OrderDto.CreateResponse response = orderService.createOrder(authUser);
        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }


    // 사장님 - 가게 주문 목록 조회
    @GetMapping("/owner/stores/{storeId}/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.OwnerOrderListResponse>>> getStoreOrders(
            @Auth AuthUser authUser,
            @PathVariable Long storeId
    ) {
        List<OrderDto.OwnerOrderListResponse> response = orderService.getOrdersByStore(authUser, storeId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }


    // 유저 - 내 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.UserOrderHistoryResponse>>> getUserOrders(
            @Auth AuthUser authUser) {
        List<OrderDto.UserOrderHistoryResponse> response = orderService.getOrdersByUser(authUser);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 주문 상태 변경 (사장님만)
    @PatchMapping("/owner/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto.StatusUpdateResponse>> updateOrderStatus(
            @Auth AuthUser authUser,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderDto.StatusUpdateRequest request
    ) {
        orderService.updateOrderStatus(authUser, orderId, request);
        OrderDto.StatusUpdateResponse response = new OrderDto.StatusUpdateResponse("주문 상태가 변경되었습니다.");
        return ApiResponse.onSuccess(SuccessStatus.OK, response);

    }



}
