package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {



    @Query("SELECT o FROM Order o "+"WHERE o.id = :orderId "+"AND o.store.id = :storeId "+"AND o.user.id = :userId "+"AND o.status = :status "+"AND o.review IS NULL")
    Optional<Order> findValidOrderReview(@Param("orderId") Long orderId, @Param("storeId") Long storeId, @Param("userId") Long userId, @Param("status") OrderStatus status);

}
