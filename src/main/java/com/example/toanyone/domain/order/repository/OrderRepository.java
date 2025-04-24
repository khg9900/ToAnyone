package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
  주문 엔티티에 대한 기본 CRUD 기능을 제공하는 JPA 리포지토리
  - 주문 저장, 조회 등에 사용됨
*/
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {



    @Query("SELECT o FROM Order o "+"WHERE o.id = :orderId "+"AND o.user.id = :userId "+"AND o.status = 'DELIVERED' "+"AND o.review IS NULL")
    Optional<Order> findReviewableOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

}
