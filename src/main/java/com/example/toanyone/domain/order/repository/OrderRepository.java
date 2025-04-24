package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
  주문 엔티티에 대한 기본 CRUD 기능을 제공하는 JPA 리포지토리
  - 주문 저장, 조회 등에 사용됨
*/
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
