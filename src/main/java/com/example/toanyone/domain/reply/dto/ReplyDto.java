package com.example.toanyone.domain.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReplyDto {
    private final Long userId;
    private final String content;
    private final LocalDateTime updateAt;
}
