package com.example.toanyone.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String content;

    public ReplyRequestDto(String content) {
        this.content = content;
    }
}
