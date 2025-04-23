package com.example.toanyone.domain.review.dto;

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
    private int rating;

    @NotBlank
    @Size(min = 10, message = "최소 10자 이상 입력해주세요.")
    private String content;

    private boolean visible = true;
}
