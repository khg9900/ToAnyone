package com.example.toanyone.domain.review.controller;

import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> review(@PathVariable Long storeId,
                                                    @RequestParam Long userId, // 유저 아이디 임시로 직접 받기
                                                    @Valid @RequestBody ReviewCreateRequestDto requestDto) {

        ReviewResponseDto response = reviewService.createReview(storeId, userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
