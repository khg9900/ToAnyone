package com.example.toanyone.domain.order.service;

import com.example.toanyone.domain.order.entity.OrderLog;
import com.example.toanyone.domain.order.repository.OrderLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderLogService {

    private final OrderLogRepository orderLogRepository;

    public void saveLog(Long storeId, Long orderId, String action) {
        OrderLog log = new OrderLog(storeId, orderId, action);
        orderLogRepository.save(log);
    }
}

