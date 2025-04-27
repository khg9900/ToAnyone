package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.repository.ReviewRepository;
import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class CreateReviewTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void 리뷰_생성_성공() {
        // Given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;

        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User user = new User(
                "eeeee@gmail.com",     // email
                "1234Aaa.",            // password
                "이름",                // name
                UserRole.USER,         // userRole
                "별명",                 // nickname
                "010-1234-1234",        // phone
                "서울시 강남구",         // address
                "FEMALE",              // gender (String으로, "FEMALE" 또는 "MALE")
                "2000-01-01"           // birth (String으로, "yyyy-MM-dd" 형식)
        );

        ReflectionTestUtils.setField(user, "id", userId);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Order order = Order.builder()
                .store(store)
                .user(user)
                .totalPrice(15000)
                .deliveryFee(3000)
                .status(OrderStatus.DELIVERED)
                .review(null)
                .build();

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "처음 주문했는데 맛있네요 또 시킬게요", true);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(orderRepository.findReviewableOrder(orderId, userId)).willReturn(Optional.of(order));

        // When
        ReviewResponseDto result = reviewService.createReview(storeId, orderId, authUser, requestDto);

        // Then
        assertEquals("리뷰가 작성되었습니다.", result.getMessage());
    }

    @Test
    void 주문이_없는_경우_리뷰_생성_실패() {
        // Given
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;

        AuthUser authUser = new AuthUser(userId, "user@test.com", UserRole.USER.name());

        User user = new User(
                "eeeee@gmail.com",     // email
                "1234Aaa.",            // password
                "이름",                // name
                UserRole.USER,         // userRole
                "별명",                 // nickname
                "010-1234-1234",        // phone
                "주소",                 // address
                "2000",                 // birthYear (String)
                "01"                    // birthMonth (String)
        );

        ReflectionTestUtils.setField(user, "id", userId);

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "처음 주문했는데 맛있네요 또 또 또 시킬게요", true);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(orderRepository.findReviewableOrder(orderId, userId)).willReturn(Optional.empty());

        // When
        ApiException exception = assertThrows(ApiException.class,
                () -> reviewService.createReview(storeId, orderId, authUser, requestDto));

        // Then
        assertEquals("리뷰 작성 조건을 만족하지 않는 주문입니다.", exception.getMessage());
    }
}
