package com.example.toanyone.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReplyRequestDto {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String content;
}
