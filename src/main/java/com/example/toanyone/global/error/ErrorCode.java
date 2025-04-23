package com.example.toanyone.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 1000: auth 에러
     */
    TOKEN_INVALID(1001, HttpStatus.UNAUTHORIZED.value(), "잘못된 토큰입니다."),


    /**
     * 2000: user 에러
     */
    USER_NOT_FOUND(2001, HttpStatus.NOT_FOUND.value(), "존재하지 않는 사용자입니다."),



    ;

    private final int code;
    private final int status;
    private final String message;
}

