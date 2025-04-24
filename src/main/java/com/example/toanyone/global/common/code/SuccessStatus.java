package com.example.toanyone.global.common.code;

import com.example.toanyone.global.common.dto.ReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 1000: 성공 코드
    OK(HttpStatus.OK, "1000", "성공"),
    CREATED(HttpStatus.CREATED, "1001", "생성 완료");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
            .isSuccess(true)
            .code(code)
            .message(message)
            .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
            .isSuccess(true)
            .httpStatus(httpStatus)
            .code(code)
            .message(message)
            .build();
    }
}