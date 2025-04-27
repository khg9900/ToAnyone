package com.example.toanyone.global.common.code;

import com.example.toanyone.global.common.dto.ErrorReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 2000: auth 에러 코드
    ACCESS_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "2001", "access 토큰이 필요합니다."),
    INVALID_JWT_CLAIMS(HttpStatus.BAD_REQUEST, "2002", "JWT claims 정보가 유효하지 않습니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "2003", "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "2004", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "2005", "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "2006", "유효하지 않은 JWT 토큰입니다."),
    TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "2007", "JWT 토큰이 필요합니다."),
    REFRESH_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "2008", "refresh 토큰이 필요합니다."),
    INVALID_JWT_TYPE(HttpStatus.BAD_REQUEST, "2009", "유효하지 않은 JWT 토큰 타입입니다."),


    // 3000: user 에러 코드
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "3001","고객 정보가 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "3002","이미 가입된 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "3003","사용중인 닉네임입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "3004","이미 가입된 전화번호입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "3005", "비밀번호가 올바르지 않습니다"),
    PASSWORD_SAME_AS_CURRENT(HttpStatus.UNAUTHORIZED, "3006", "새 비밀번호는 현재 비밀번호와 동일할 수 없습니다"),
    USER_ALREADY_DELETED(HttpStatus.GONE, "3007", "이미 탈퇴한 회원입니다."),

    OWNER_HAS_ACTIVE_STORE(HttpStatus.CONFLICT, "3003", "운영 중인 가게가 존재하여 회원 탈퇴가 불가합니다."),

    // 4000: menu 에러코드
    MENU_ALREADY_EXISTS(HttpStatus.CONFLICT,"4001","이미 존재하는 메뉴입니다."),
    MENU_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "4002", "이미 삭제된 메뉴입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "4003","존재하지 않는 메뉴입니다."),
    CART_ITEMS_NOT_FOUND(HttpStatus.NOT_FOUND, "4004", "장바구니를 찾을 수 없습니다."),

    // 5000: Store 에러코드
    STORE_NO_PERMISSION(HttpStatus.FORBIDDEN, "5001","가게 생성 권한이 없습니다."),
    STORE_MAX_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "5002","가게는 최대 3개까지 등록 가능합니다."),
    STORE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "5003","이미 존재하는 가게이름입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "5004","가게 정보가 없습니다."),
    STORE_SEARCH_NO_MATCH(HttpStatus.NOT_FOUND, "5005","검색한 단어가 포함된 가게명이 존재하지 않습니다."),
    STORE_SHUT_DOWN(HttpStatus.BAD_REQUEST, "5006", "폐업된 가게입니다."),
    STORE_CLOSED(HttpStatus.BAD_REQUEST, "5007", "영업 종료된 가게입니다.");





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
