package com.example.toanyone.domain.review.dto;

import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.dto.ReplyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewCheckResponseDto {
    private Long id;
    private Integer rating;
    private String content;
    private Boolean visible;
    private LocalDateTime updateAt;
    private ReplyDto reply; // 사장님 댓글
}

