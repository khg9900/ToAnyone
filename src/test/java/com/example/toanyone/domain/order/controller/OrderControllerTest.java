package com.example.toanyone.domain.order.controller;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.service.OrderService;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.auth.jwt.JwtUtil;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import com.example.toanyone.global.common.error.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtUtil jwtUtil; // JwtUtil 빈을 MockBean으로 추가

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("주문 생성 API")
    class CreateOrderTest {

        @Test
        @DisplayName("주문 생성 성공")
        void createOrderSuccess() throws Exception {
            // given
            OrderDto.CreateResponse createResponse = new OrderDto.CreateResponse(1L, 10L, null, "WAITING");
            when(orderService.createOrder(any(AuthUser.class))).thenReturn(createResponse);

            // when & then
            mockMvc.perform(post("/orders")
                            .header("Authorization", "Bearer mockToken")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.storeId").value(1L))
                    .andExpect(jsonPath("$.data.orderId").value(10L));
        }

        @Test
        @DisplayName("주문 생성 실패 - 유저 없음")
        void createOrderFail_userNotFound() throws Exception {
            // given
            when(orderService.createOrder(any(AuthUser.class)))
                    .thenThrow(new ApiException(ErrorStatus.USER_NOT_FOUND));

            // when & then
            mockMvc.perform(post("/orders")
                            .header("Authorization", "Bearer mockToken")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }
    }
}
