package com.example.toanyone.domain.order.repository;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Order 저장 및 조회 성공 테스트")
    void saveAndFindOrder() {
        // given
        User user = createUser();
        Store store = createStore(user);
        Order order = createOrder(user, store);

        entityManager.persist(user);
        entityManager.persist(store);
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getUser().getId()).isEqualTo(user.getId());
        assertThat(orders.get(0).getStore().getId()).isEqualTo(store.getId());
    }

    @Test
    @DisplayName("storeId로 주문 목록 조회 성공 테스트")
    void findAllByStoreId() {
        // given
        User user = createUser();
        Store store = createStore(user);
        Order order = createOrder(user, store);

        entityManager.persist(user);
        entityManager.persist(store);
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Order> orders = orderRepository.findAllByStoreId(store.getId());

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getStore().getId()).isEqualTo(store.getId());
    }

    @Test
    @DisplayName("userId로 주문 목록 조회 성공 테스트")
    void findAllByUserId() {
        // given
        User user = createUser();
        Store store = createStore(user);
        Order order = createOrder(user, store);

        entityManager.persist(user);
        entityManager.persist(store);
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Order> orders = orderRepository.findAllByUserId(user.getId());

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("User 객체로 주문 목록 조회 성공 테스트")
    void findAllByUser() {
        // given
        User user = createUser();
        Store store = createStore(user);
        Order order = createOrder(user, store);

        entityManager.persist(user);
        entityManager.persist(store);
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Order> orders = orderRepository.findAllByUser(user);

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("리뷰 가능한 주문 조회 성공 테스트 (status = DELIVERED, review = null)")
    void findReviewableOrder() {
        // given
        User user = createUser();
        Store store = createStore(user);
        Order order = createOrder(user, store);
        order.changeStatus(OrderStatus.DELIVERED); // 주문 상태를 DELIVERED로 변경

        entityManager.persist(user);
        entityManager.persist(store);
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<Order> foundOrder = orderRepository.findReviewableOrder(order.getId(), user.getId());

        // then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(foundOrder.get().getReview()).isNull();
    }

    // =========================
    // Fixture 메서드
    // =========================

    private User createUser() {
        return new User(
                "test@example.com",
                "password123!",
                "테스트유저",
                UserRole.USER,
                "testnickname",
                "010-1234-5678",
                "서울특별시 강남구",
                "MALE",
                "1995-05-21"
        );
    }

    private Store createStore(User user) {
        StoreRequestDto.Create dto = StoreRequestDto.Create.builder()
                .name("Test Store")
                .address("서울특별시 강남구")
                .openTime("09:00")
                .closeTime("23:00")
                .deliveryFee(3000)
                .minOrderPrice(15000)
                .notice("가게 공지사항입니다.")
                .status("OPEN")
                .phone("010-0000-0000")
                .build();

        return new Store(user, dto);
    }

    private Order createOrder(User user, Store store) {
        return Order.builder()
                .user(user)
                .store(store)
                .totalPrice(20000)
                .deliveryFee(3000)
                .status(OrderStatus.WAITING)
                .build();
    }
}
