package com.example.toanyone.domain.order.aop;

import com.example.toanyone.domain.order.dto.OrderDto;
import com.example.toanyone.domain.order.service.OrderLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OrderLogAspect {

    private final OrderLogService orderLogService;

    @Around("execution(* com.example.toanyone.domain.order.service.OrderServiceImpl.createOrder(..)) || " +
            "execution(* com.example.toanyone.domain.order.service.OrderServiceImpl.updateOrderStatus(..))")
    public Object logOrderAction(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 정보 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 요청 로그 출력
        log.info("[요청] - 요청 시각: {}, 요청 URL: {}, 메서드: {}, 파라미터: {}",
                LocalDateTime.now(), requestURI, methodName, argsToString(args));

        // 실제 서비스 메서드 실행
        Object result = joinPoint.proceed();

        // 응답 로그 출력
        log.info("[응답] - 응답 시각: {}, 메서드: {}, 반환값: {}",
                LocalDateTime.now(), methodName, result);

        // DB 로그 저장
        if (result instanceof OrderDto.CreateResponse response) {
            orderLogService.saveLog(response.getStoreId(), response.getOrderId(), "CREATE_ORDER");
        } else if (result instanceof OrderDto.StatusUpdateResponse response) {
            String updatedStatus = response.getUpdatedStatus(); // 변경된 주문 상태 가져오기
            orderLogService.saveLog(
                    response.getStoreId(),
                    response.getOrderId(),
                    "UPDATE_ORDER_STATUS (" + updatedStatus + ")"
            );
        }

        return result;
    }

    private String argsToString(Object[] args) {
        if (args == null || args.length == 0) return "없음";
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg).append(" | ");
        }
        return sb.toString();
    }
}
