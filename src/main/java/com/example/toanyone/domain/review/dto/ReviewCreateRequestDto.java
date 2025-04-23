package com.example.toanyone.domain.review.dto;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.user.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 리뷰 생성 요청 DTO
 * */
@Getter
public class ReviewCreateRequestDto {

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(min = 10, message = "최소 10자 이상 입력해주세요.")
    private String content;

    private Boolean visible = true;
}
