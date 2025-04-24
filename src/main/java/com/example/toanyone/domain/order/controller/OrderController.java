package com.example.toanyone.domain.order.controller;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.service.OrderService;
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

    // 주문 생성 (POST /orders)

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderDto.CreateResponse>> createOrder(
            @RequestParam Long cartId) {
        OrderDto.CreateResponse response = orderService.createOrder(null, cartId);
        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }

    // 가게 주문 목록 조회 (GET /owner/stores/{storeId}/orders)

    @GetMapping("/owner/stores/{storeId}/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.OwnerOrderListResponse>>> getStoreOrders(
            @PathVariable Long storeId) {
        List<OrderDto.OwnerOrderListResponse> response = orderService.getOrdersByStore(storeId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 내 주문 내역 조회 (GET /orders)

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.UserOrderHistoryResponse>>> getUserOrders() {
        List<OrderDto.UserOrderHistoryResponse> response = orderService.getOrdersByUser();
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }
}
