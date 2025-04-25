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
            @Auth AuthUser authUser,
            @RequestBody @Valid OrderDto.CreateRequest request
    ) {
        OrderDto.CreateResponse response = orderService.createOrder(authUser, request.getCartId());
        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }


    // 사장님 - 가게 주문 목록 조회
    @GetMapping("/owner/stores/{storeId}/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.OwnerOrderListResponse>>> getStoreOrders(
            @PathVariable Long storeId
    ) {
        List<OrderDto.OwnerOrderListResponse> response = orderService.getOrdersByStore(storeId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 유저 - 내 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.UserOrderHistoryResponse>>> getUserOrders(
            @Auth AuthUser authUser) {
        List<OrderDto.UserOrderHistoryResponse> response = orderService.getOrdersByUser(authUser);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

}
