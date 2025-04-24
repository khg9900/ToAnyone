package com.example.toanyone.global.common.code;

import com.example.toanyone.global.common.dto.ErrorReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 1000: auth 에러
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "1001", "잘못된 토큰입니다."),

    // 2000: user 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "2001","존재하지 않는 사용자입니다.")

    // 3000:

    // 4000:

    // 5000:


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
            .isSuccess(false)
            .code(code)
            .message(message)
            .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
            .isSuccess(false)
            .httpStatus(httpStatus)
            .code(code)
            .message(message)
            .build();
    }
}
