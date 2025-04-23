package com.example.toanyone.domain.review.controller;

import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.service.ReviewService;
import com.example.toanyone.global.common.annotation.Auth;
import com.example.toanyone.global.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> review(@PathVariable Long storeId,
                                                    @Auth AuthUser authUser,
                                                    @Valid @RequestBody ReviewCreateRequestDto requestDto) {

        ReviewResponseDto response = reviewService.createReview(storeId, authUser, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{storeId}/reviews")
    public ResponseEntity<Page<ReviewCheckResponseDto>> reviewCheck(@PathVariable Long storeId,
                                                              @Auth AuthUser authUser,
                                                              @RequestParam(required = false) List<Integer> rating,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<ReviewCheckResponseDto> review = reviewService.checkReview(storeId, authUser, rating, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }
}