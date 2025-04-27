package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("주문 저장 테스트")
    class SaveOrderTest {

        @Test
        @DisplayName("주문 저장 성공")
        @Rollback(false)
        void saveOrderSuccess() {
            // given
            User user = new User();
            Store store = new Store();
            Order order = Order.builder()
                    .user(user)
                    .store(store)
                    .status(OrderStatus.WAITING) //
                    .build();

            // when
            Order savedOrder = orderRepository.save(order);

            // then
            assertThat(savedOrder).isNotNull();
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.WAITING);
        }
    }

    @Nested
    @DisplayName("주문 조회 테스트")
    class FindOrderTest {

        @Test
        @DisplayName("주문 조회 성공")
        void findOrderSuccess() {
            // given
            User user = new User();
            Store store = new Store();
            Order order = Order.builder()
                    .user(user)
                    .store(store)
                    .status(OrderStatus.WAITING)
                    .build();

            Order savedOrder = orderRepository.save(order);

            // when
            Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

            // then
            assertThat(foundOrder).isNotNull();
            assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
        }

        @Test
        @DisplayName("주문 조회 실패 - 존재하지 않는 ID")
        void findOrderFail_NotFound() {
            // given
            Long invalidOrderId = 999L;

            // when
            Order foundOrder = orderRepository.findById(invalidOrderId).orElse(null);

            // then
            assertThat(foundOrder).isNull();
        }
    }

    @Nested
    @DisplayName("유저 ID로 주문 목록 조회 테스트")
    class FindOrdersByUserTest {

        @Test
        @DisplayName("유저 ID로 주문 목록 조회 성공")
        void findOrdersByUserSuccess() {
            // given
            User user = new User();
            ReflectionTestUtils.setField(user, "id", 1L);

            Store store = new Store();
            ReflectionTestUtils.setField(store, "id", 100L);

            Order order1 = Order.builder()
                    .user(user)
                    .store(store)
                    .status(OrderStatus.WAITING)
                    .build();

            Order order2 = Order.builder()
                    .user(user)
                    .store(store)
                    .status(OrderStatus.COOKING)
                    .build();

            orderRepository.save(order1);
            orderRepository.save(order2);

            // when
            List<Order> orders = orderRepository.findAllByUserId(user.getId());

            // then
            assertThat(orders).hasSize(2);
            assertThat(orders.get(0).getUser().getId()).isEqualTo(1L);
            assertThat(orders.get(1).getUser().getId()).isEqualTo(1L);
        }
    }
}
