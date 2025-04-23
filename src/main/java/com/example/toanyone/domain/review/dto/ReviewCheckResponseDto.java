package com.example.toanyone.domain.review.dto;

import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.dto.ReplyResponseDto;
import com.example.toanyone.domain.reply.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewCheckResponseDto {
    private final Long id;
    private final Integer rating;
    private final String content;
    private final Boolean visible;
    private final LocalDateTime updateAt;
    private final ReplyDto reply; // 사장님 댓글
}

