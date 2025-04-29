package com.example.toanyone.domain.order.controller;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.service.OrderService;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import com.example.toanyone.global.common.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "user@example.com", "USER");
    }

    @Nested
    @DisplayName("주문 생성 API")
    class CreateOrderTest {

        @Test
        @DisplayName("주문 생성 성공")
        void createOrderSuccess() {
            OrderDto.CreateResponse createResponse = new OrderDto.CreateResponse(1L, 10L, null, "WAITING");

            when(orderService.createOrder(authUser)).thenReturn(createResponse);

            ResponseEntity<ApiResponse<OrderDto.CreateResponse>> response = orderController.createOrder(authUser);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody().getPayload().getStoreId()).isEqualTo(1L);
            assertThat(response.getBody().getPayload().getOrderId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("주문 생성 실패 - 유저 없음")
        void createOrderFail_userNotFound() {
            when(orderService.createOrder(authUser)).thenThrow(new ApiException(ErrorStatus.USER_NOT_FOUND));

            ApiException exception = assertThrows(ApiException.class, () -> {
                orderController.createOrder(authUser);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("가게 주문 목록 조회 API")
    class GetStoreOrdersTest {

        @Test
        @DisplayName("가게 주문 목록 조회 성공")
        void getStoreOrdersSuccess() {
            List<OrderDto.OwnerOrderListResponse> responseList = List.of();

            when(orderService.getOrdersByStore(authUser, 1L)).thenReturn(responseList);

            ResponseEntity<ApiResponse<List<OrderDto.OwnerOrderListResponse>>> response = orderController.getStoreOrders(authUser, 1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getPayload()).isNotNull();
        }

        @Test
        @DisplayName("가게 주문 목록 조회 실패 - 가게 없음")
        void getStoreOrdersFail_storeNotFound() {
            when(orderService.getOrdersByStore(authUser, 1L))
                    .thenThrow(new ApiException(ErrorStatus.STORE_NOT_FOUND));

            ApiException exception = assertThrows(ApiException.class, () -> {
                orderController.getStoreOrders(authUser, 1L);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus.STORE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("내 주문 내역 조회 API")
    class GetUserOrdersTest {

        @Test
        @DisplayName("내 주문 내역 조회 성공")
        void getUserOrdersSuccess() {
            List<OrderDto.UserOrderHistoryResponse> responseList = List.of();

            when(orderService.getOrdersByUser(authUser)).thenReturn(responseList);

            ResponseEntity<ApiResponse<List<OrderDto.UserOrderHistoryResponse>>> response = orderController.getUserOrders(authUser);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getPayload()).isNotNull();
        }

        @Test
        @DisplayName("내 주문 내역 조회 실패 - 유저 없음")
        void getUserOrdersFail_userNotFound() {
            when(orderService.getOrdersByUser(authUser))
                    .thenThrow(new ApiException(ErrorStatus.USER_NOT_FOUND));

            ApiException exception = assertThrows(ApiException.class, () -> {
                orderController.getUserOrders(authUser);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 API")
    class UpdateOrderStatusTest {

        @Test
        @DisplayName("주문 상태 변경 성공")
        void updateOrderStatusSuccess() {
            OrderDto.StatusUpdateRequest request = OrderDto.StatusUpdateRequest.builder()
                    .status("COOKING")
                    .build();

            OrderDto.StatusUpdateResponse updateResponse = OrderDto.StatusUpdateResponse.builder()
                    .orderId(10L)
                    .updatedStatus("COOKING")
                    .build();

            when(orderService.updateOrderStatus(authUser, 10L, request)).thenReturn(updateResponse);

            ResponseEntity<ApiResponse<OrderDto.StatusUpdateResponse>> response =
                    orderController.updateOrderStatus(authUser, 10L, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getPayload().getOrderId()).isEqualTo(10L);
            assertThat(response.getBody().getPayload().getUpdatedStatus()).isEqualTo("COOKING");
        }

        @Test
        @DisplayName("주문 상태 변경 실패 - 주문 없음")
        void updateOrderStatusFail_orderNotFound() {
            OrderDto.StatusUpdateRequest request = OrderDto.StatusUpdateRequest.builder()
                    .status("COOKING")
                    .build();

            when(orderService.updateOrderStatus(authUser, 10L, request))
                    .thenThrow(new ApiException(ErrorStatus.ORDER_NOT_FOUND));

            ApiException exception = assertThrows(ApiException.class, () -> {
                orderController.updateOrderStatus(authUser, 10L, request);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus.ORDER_NOT_FOUND);
        }
    }
}
