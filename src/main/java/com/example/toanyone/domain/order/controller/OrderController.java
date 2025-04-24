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

// 주문 관련 요청을 처리하는 컨트롤러

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto.CreateResponse>> createOrder(
            @RequestParam Long cartId) {
        OrderDto.CreateResponse response = orderService.createOrder(null, cartId);
        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }

    // [사장님용] 가게 주문 목록 조회

    @GetMapping("/owner/stores/{storeId}/orders")
    public ResponseEntity<ApiResponse<List<OrderDto.OwnerOrderListResponse>>> getStoreOrders(
            @PathVariable Long storeId) {
        List<OrderDto.OwnerOrderListResponse> response = orderService.getOrdersByStore(storeId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    //[사용자용] 내 주문 내역 조회

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto.UserOrderHistoryResponse>>> getUserOrders() {
        List<OrderDto.UserOrderHistoryResponse> response = orderService.getOrdersByUser();
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 주문
    //
}
