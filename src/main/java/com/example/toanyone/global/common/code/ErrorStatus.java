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
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "3002", "비밀번호가 올바르지 않습니다"),
    OWNER_HAS_ACTIVE_STORE(HttpStatus.CONFLICT, "3003", "운영 중인 가게가 존재하여 회원 탈퇴가 불가합니다."),

    // 4000: menu 에러코드
    MENU_ALREADY_EXISTS(HttpStatus.CONFLICT,"4001","이미 존재하는 메뉴입니다."),
    MENU_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "4002", "이미 삭제된 메뉴입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "4003","존재하지 않는 메뉴입니다."),
  
    // 5000: Store 에러코드
    STORE_NO_PERMISSION(HttpStatus.FORBIDDEN,"5001","가게 생성 권한이 없습니다."),
    STORE_MAX_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,"5002","가게는 최대 3개까지 등록 가능합니다."),
    STORE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"5003","이미 존재하는 가게이름입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND,"5004","생성된 가게가 없습니다."),
    STORE_SEARCH_NO_MATCH(HttpStatus.NOT_FOUND,"5005","검색한 단어가 포함된 가게명이 존재하지 않습니다."),
    STORE_SHUT_DOWN(HttpStatus.BAD_REQUEST, "5006", "폐업된 가게입니다."),
    STORE_CLOSED(HttpStatus.BAD_REQUEST, "5007", "영업 종료된 가게입니다."),
    STORE_FORBIDDEN(HttpStatus.FORBIDDEN, "5008", "가게 접근 권한이 없습니다."),
    STORE_INVALID_STATUS(HttpStatus.BAD_REQUEST, "5009", "OPEN, SOLD_OUT, TEMP_CLOSED, CLOSE 중 하나를 선택해주세요."),
    STORE_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "5010", "이미 폐업된 가게입니다."),
    NOT_STORE_OWNER(HttpStatus.BAD_REQUEST, "5011", "가게의 주인이 아니면 접근할 수 없습니다"),

    // 6000: review
    REVIEW_CREATE_FORBIDDEN(HttpStatus.FORBIDDEN, "6001", "리뷰 작성 조건을 만족하지 않는 주문입니다."),
    ORDER_STORE_MISMATCH(HttpStatus.BAD_REQUEST, "6002", "해당 가게의 주문이 아닙니다."),
    REVIEW_INVALID_RATING(HttpStatus.BAD_REQUEST, "6003", "별점은 1점에서 5점 사이만 조회 가능합니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "6004", "해당 리뷰가 존재하지 않습니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.BAD_REQUEST, "6005", "해당 권한이 없습니다."),
    REVIEW_STORE_MISMATCH(HttpStatus.BAD_REQUEST, "6006", "해당 가게의 리뷰가 아닙니다."),
  
    //7000 : cart 에러코드
    CART_ITEMS_NOT_FOUND(HttpStatus.NOT_FOUND, "7001", "장바구니에서 해당 품목을 찾을 수 없습니다."),
    CART_ITEM_QUANTITY_UNDERFLOW(HttpStatus.BAD_REQUEST, "7002", "차감하려는 수량이 담긴 수량보다 많습니다."),

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
