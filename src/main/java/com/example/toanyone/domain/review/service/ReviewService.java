package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;

public interface ReviewService  {
    ReviewResponseDto createReview(Long storeId, Long userId, ReviewCreateRequestDto dto);
}
