package com.example.toanyone.global.common.code;

import com.example.toanyone.global.common.dto.ErrorReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 2000: auth 에러 코드
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "2001", "잘못된 토큰입니다."),

    // 3000: user 에러 코드
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "3001","고객 정보가 없습니다."),

    // 4000: menu 에러코드
    MENU_ALREADY_EXISTS(HttpStatus.CONFLICT,"4001","이미 존재하는 메뉴입니다."),
    MENU_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "4002", "이미 삭제된 메뉴입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "4003","존재하지 않는 메뉴입니다."),

    // 5000: Store 에러코드
    STORE_NO_PERMISSION(HttpStatus.FORBIDDEN,"5001","가게 생성 권한이 없습니다."),
    STORE_MAX_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,"5002","가게는 최대 3개까지 등록 가능합니다."),
    STORE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"5003","이미 존재하는 가게이름입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND,"5004","생성된 가게가 없습니다."),
    STORE_SEARCH_NO_MATCH(HttpStatus.NOT_FOUND,"5005","검색한 단어가 포함된 가게명이 존재하지 않습니다.");



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
