package com.example.toanyone.domain.order.aop;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.entity.OrderLog;
import com.example.toanyone.domain.order.repository.OrderLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Aspect
public class OrderLogAspect {

    private final OrderLogRepository orderLogRepository;

    @After("execution(* com.example.toanyone.domain.order.service.OrderServiceImpl.createOrder(..)) || " +
            "execution(* com.example.toanyone.domain.order.service.OrderServiceImpl.updateOrderStatus(..))")
    public Object logOrderAction(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();
        OrderLog orderLog = new OrderLog();

        // result 타입 캐스팅
        if (result instanceof OrderDto.CreateResponse response) {
            orderLog = new OrderLog(response.getStoreId(), response.getOrderId(), response.getStatus(), LocalDateTime.now());
        }
        if (result instanceof OrderDto.StatusUpdateResponse response) {
            orderLog = new OrderLog(response.getStoreId(), response.getOrderId(), response.getUpdatedStatus(), LocalDateTime.now());
        }

        // 요청 로그 출력
        log.info("[ORDER LOGGING] Store ID: {}, Order ID: {}, Order Status: {}, TimeStamp: {}",
            orderLog.getStoreId(), orderLog.getOrderId(), orderLog.getStatus(), orderLog.getLogTime());

        // DB 저장
        orderLogRepository.save(orderLog);

        return result;
    }
}
