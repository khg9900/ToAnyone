package com.example.toanyone.domain.order.aop;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.service.OrderLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OrderLogAspect {

    private final OrderLogService orderLogService;

    // 주문 생성 시
    @AfterReturning(pointcut = "execution(* com.example.toanyone.domain.order.service.OrderServiceImpl.createOrder(..))", returning = "response")
    public void logCreateOrder(JoinPoint joinPoint, Object response) {
        if (response instanceof OrderDto.CreateResponse createResponse) {
            // createResponse 안에 orderId, createdAt, status가 들어있음
            Long orderId = createResponse.getOrderId();
            Long storeId = createResponse.getStoreId();

            // storeId를 가져와야 하는데, 현재 AOP에서는 파라미터로 받은 AuthUser밖에 없음.
            // (storeId는 알 수 없음)

            // 👉 정리: storeId를 알 방법이 없으니까 일단 orderId만 찍고, storeId는 null로 처리
            orderLogService.saveLog(storeId, orderId, "CREATE_ORDER");

            log.info("[주문 생성] orderId: {}", orderId);
        }
    }


    // 주문 상태 변경 시
    @AfterReturning(pointcut = "execution(* com.example.toanyone.domain.order.service.OrderServiceImpl.updateOrderStatus(..))", returning = "order")
    public void logUpdateOrderStatus(JoinPoint joinPoint, Object order) {
        if (order instanceof Order) {
            Order updatedOrder = (Order) order;
            orderLogService.saveLog(updatedOrder.getStore().getId(), updatedOrder.getId(), "UPDATE_ORDER_STATUS");
            log.info("[주문 상태 변경] storeId: {}, orderId: {}", updatedOrder.getStore().getId(), updatedOrder.getId());
        }
    }
}
