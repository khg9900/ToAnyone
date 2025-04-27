package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
}
