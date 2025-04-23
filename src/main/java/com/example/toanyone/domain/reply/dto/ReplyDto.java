package com.example.toanyone.domain.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReplyDto {
    private Long userId;
    private String content;
    private LocalDateTime updateAt;
}
