package com.example.toanyone.domain.review.controller;

import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.service.ReviewService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;


    /**
     * 리뷰생성
     * */
    @PostMapping("/{storeId}/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<Void>> reviewCreate(
        @PathVariable Long storeId,
        @PathVariable Long orderId,
        @Auth AuthUser authUser,
        @Valid @RequestBody ReviewCreateRequestDto requestDto
    ) {
        reviewService.createReview(storeId, orderId, authUser, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.REVIEW_CREATED);
    }

    /**
     * 리뷰수정
     * */
    @PatchMapping("/{storeId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> reviewUpdate(
        @PathVariable Long storeId,
        @PathVariable Long reviewId,
        @Auth AuthUser authUser,
        @Valid @RequestBody ReviewCreateRequestDto requestDto
    ){
        reviewService.updateReview(storeId, reviewId, authUser, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.REVIEW_UPDATED);
    }

    /**
     * 리뷰조회
     * */
    @GetMapping("/{storeId}/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewCheckResponseDto>>> reviewCheck(
        @PathVariable Long storeId,
        @Auth AuthUser authUser,
        @RequestParam(required = false) List<Integer> rating,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewCheckResponseDto> responseDto = reviewService.checkReview(storeId, authUser, rating, page, size);
        return ApiResponse.onSuccess(SuccessStatus.REVIEW_FETCHED, responseDto);
    }

    /**
     * 리뷰삭제
     * */
    @DeleteMapping("/{storeId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> reviewDelete(
        @PathVariable Long storeId,
        @PathVariable Long reviewId,
        @Auth AuthUser authUser
    ) {
        reviewService.deleteReview(storeId, reviewId, authUser);
        return ApiResponse.onSuccess(SuccessStatus.REVIEW_DELETED);
    }
}