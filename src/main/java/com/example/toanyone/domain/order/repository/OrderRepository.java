package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {




    Optional<Order> findByStoreIdAndUserIdAndStatusAndReviewIsNull(Long storeId, Long userId, OrderStatus status);

}
