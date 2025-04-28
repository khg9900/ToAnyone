package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.global.auth.dto.AuthUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService  {

    void createReview(Long storeId, Long orderId, AuthUser authUser, ReviewCreateRequestDto dto);

    Page<ReviewCheckResponseDto> checkReview(Long storeId, AuthUser authUser, List<Integer> rating, int page, int size);

    void updateReview(Long storeId, Long reviewId, AuthUser authUser,ReviewCreateRequestDto requestDto);

    void deleteReview(Long storeId, Long reviewId, AuthUser authUser);
}
