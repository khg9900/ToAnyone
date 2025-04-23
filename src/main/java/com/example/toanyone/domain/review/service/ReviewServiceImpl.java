package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.entity.Reply;
import com.example.toanyone.domain.reply.repository.ReplyRepository;
import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.repository.ReviewRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.common.dto.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReviewResponseDto createReview(Long storeId, AuthUser authUser, ReviewCreateRequestDto request){

        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("not found user"));

        Order order = orderRepository.findByStoreIdAndUserIdAndStatusAndReviewIsNull(storeId, user.getId(), OrderStatus.DELIVERED)
                .orElseThrow(() -> new IllegalArgumentException("배달 완료된 주문만 리뷰를 작성할 수 있습니다."));

        Review review = new Review(
                order,
                user,
                request.getRating(),
                request.getContent(),
                request.getVisible()
        );

        reviewRepository.save(review);

        return new ReviewResponseDto("리뷰가 작성되었습니다.");
    }

    @Override
    public Page<ReviewCheckResponseDto> checkReview(Long storeId, AuthUser authUser, List<Integer> rating, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        Page<Review> review;

        if(rating != null && rating.isEmpty()){
            for(Integer r : rating) {
                if (r < 1 || r > 5) {
                    throw new IllegalArgumentException("별점은 1점에서 5점 사이만 조회 가능합니다.");
                }
            }
            review = reviewRepository.findAllStoreIdAndRating(storeId,rating,pageable);
        } else {
            review = reviewRepository.findAllByStoreId(storeId, pageable);
        }

        List<ReviewCheckResponseDto> response = new ArrayList<>();
        for(Review a18 : review.getContent()) {
            Reply reply = a18.getReply();
            ReviewCheckResponseDto rrrr = new ReviewCheckResponseDto(a18.getId(),
                    a18.getRating(),
                    a18.getContent(),
                    a18.getVisible(),
                    a18.getUpdatedAt(),
                    new ReplyDto(reply.getId(), reply.getContent(), reply.getUpdatedAt()));
            response.add(rrrr);
    }
        return new PageImpl<>(response, pageable, review.getTotalElements());
        }
}
