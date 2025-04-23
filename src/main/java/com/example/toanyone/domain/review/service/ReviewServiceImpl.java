package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    @Override
    public ReviewResponseDto createReview(Long storeId, Long userId, ReviewCreateRequestDto dto){
        Order order = orderRepository.findByStoreIdAndUserIdAndStatusAndReviewIsNull(storeId, userId, OrderStatus.DELIVERED)
                .orElseThrow(() -> new IllegalArgumentException("배달 완료된 주문만 리뷰를 작성할 수 있습니다."));

        Review review = dto.toEntity(order, order.getUser());

        reviewRepository.save(review);

        return new ReviewResponseDto("리뷰가 작성되었습니다.");

    }

}
