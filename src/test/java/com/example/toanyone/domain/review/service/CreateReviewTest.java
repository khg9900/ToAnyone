package com.example.toanyone.domain.review.service;
/*
import com.example.toanyone.domain.reply.entity.Reply;
import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.entity.Review;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;
        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User user = new User("user@test.com", "password123", "홍길동", UserRole.USER, "nickname", "010-1234-5678", "서울시 강남구", "FEMALE", "2000-01-01");
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

        // Given
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(orderRepository.findReviewableOrder(orderId, userId)).willReturn(Optional.of(order));

        // When
        ReviewResponseDto result = reviewService.createReview(storeId, orderId, authUser, requestDto);

        // Then
        assertEquals("리뷰가 작성되었습니다.", result.getMessage());
    }

    @Test
    void 주문이_없는_경우_리뷰_생성_실패() {
        Long userId = 1L;
        Long storeId = 1L;
        Long orderId = 1L;
        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User user = new User("user@test.com", "password123", "홍길동", UserRole.USER, "nickname", "010-1234-5678", "서울시 강남구", "FEMALE", "2000-01-01");
        ReflectionTestUtils.setField(user, "id", userId);

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(5, "처음 주문했는데 맛있네요 또 시킬게요", true);

        // Given
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(orderRepository.findReviewableOrder(orderId, userId)).willReturn(Optional.empty());

        // When
        ApiException exception = assertThrows(ApiException.class,
                () -> reviewService.createReview(storeId, orderId, authUser, requestDto));

        // Then
        assertEquals("리뷰 작성 조건을 만족하지 않는 주문입니다.", exception.getMessage());
    }

    @Test
    void 리뷰_전체조회_성공() {
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "user@test.com", "USER");

        // Given
        given(reviewRepository.findAllByStoreId(anyLong(), any(Pageable.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));

        // When
        Page<ReviewCheckResponseDto> result = reviewService.checkReview(storeId, authUser, null, 1, 10);

        // Then
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void 잘못된_별점_조회_시_예외발생() {
        // Given
        Long storeId = 1L;
        AuthUser authUser = new AuthUser(1L, "user@test.com", "USER");

        List<Integer> invalidRating = List.of(0, 6);  // 잘못된 별점

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> reviewService.checkReview(storeId, authUser, invalidRating, 1, 10));

        assertEquals("별점은 1점에서 5점 사이만 조회 가능합니다.", exception.getMessage());
    }

    @Test
    void 리뷰_수정_성공() {
        Long userId = 1L;
        Long storeId = 1L;
        Long reviewId = 1L;

        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User user = new User("user@test.com", "password123", "홍길동", UserRole.USER, "nickname", "010-1234-5678", "서울시 강남구", "FEMALE", "2000-01-01");
        ReflectionTestUtils.setField(user, "id", userId);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Review review = new Review();
        ReflectionTestUtils.setField(review, "user", user);
        ReflectionTestUtils.setField(review, "store", store);

        ReviewCreateRequestDto updateRequest = new ReviewCreateRequestDto(4, "리뷰 수정합니다!", true);

        // Given
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // When
        ReviewResponseDto result = reviewService.updateReview(storeId, reviewId, authUser, updateRequest);

        // Then
        assertEquals("리뷰가 성공적으로 수정되었습니다.", result.getMessage());
    }

    @Test
    void 리뷰_수정_실패_작성자가_아님() {
        Long userId = 1L;
        Long anotherUserId = 2L;
        Long storeId = 1L;
        Long reviewId = 1L;

        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User anotherUser = new User("another@test.com", "password123", "홍길순", UserRole.USER, "anotherNick", "010-1111-2222", "서울시 강남구", "FEMALE", "2000-01-01");
        ReflectionTestUtils.setField(anotherUser, "id", anotherUserId);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Review review = new Review();
        ReflectionTestUtils.setField(review, "user", anotherUser); // 다른 유저로 세팅
        ReflectionTestUtils.setField(review, "store", store);

        ReviewCreateRequestDto updateRequest = new ReviewCreateRequestDto(4, "리뷰 수정합니다!", true);

        // Given
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> reviewService.updateReview(storeId, reviewId, authUser, updateRequest));

        assertEquals("해당 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void 리뷰_삭제_성공() {
        Long userId = 1L;
        Long storeId = 1L;
        Long reviewId = 1L;

        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User user = new User("user@test.com", "password123", "홍길동", UserRole.USER, "nickname", "010-1234-5678", "서울시 강남구", "FEMALE", "2000-01-01");
        ReflectionTestUtils.setField(user, "id", userId);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Review review = new Review();
        ReflectionTestUtils.setField(review, "user", user);
        ReflectionTestUtils.setField(review, "store", store);

        Reply reply = new Reply();
        ReflectionTestUtils.setField(reply, "id", 1L);
        ReflectionTestUtils.setField(review, "reply", reply);

        // Given
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // When
        ReviewResponseDto result = reviewService.deleteReview(storeId, reviewId, authUser);

        // Then
        assertEquals("리뷰가 성공적으로 삭제되었습니다.", result.getMessage());
    }

    @Test
    void 리뷰_삭제_실패_작성자가_아님() {
        Long userId = 1L;
        Long anotherUserId = 2L;
        Long storeId = 1L;
        Long reviewId = 1L;

        AuthUser authUser = new AuthUser(userId, "user@test.com", "USER");

        User anotherUser = new User("another@test.com", "password123", "홍길순", UserRole.USER, "anotherNick", "010-1111-2222", "서울시 강남구", "FEMALE", "2000-01-01");
        ReflectionTestUtils.setField(anotherUser, "id", anotherUserId);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Review review = new Review();
        ReflectionTestUtils.setField(review, "user", anotherUser); // 다른 유저로 세팅
        ReflectionTestUtils.setField(review, "store", store);

        // Given
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // When & Then
        ApiException exception = assertThrows(ApiException.class,
                () -> reviewService.deleteReview(storeId, reviewId, authUser));

        assertEquals("해당 권한이 없습니다.", exception.getMessage());
    }
}
*/