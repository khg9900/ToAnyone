package com.example.toanyone.global.common.code;

import com.example.toanyone.global.common.dto.ReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 1000: 성공 코드
    SIGNUP_SUCCESS(HttpStatus.CREATED, "1001", "회원가입이 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "1002", "로그인에 성공하였습니다."),
    REISSUE_SUCCESS(HttpStatus.OK, "1003", "토큰이 재발급되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "1004", "로그아웃이 완료되었습니다."),

    USER_FETCHED(HttpStatus.OK, "1005", "회원 정보 조회에 성공했습니다."),
    USER_UPDATED(HttpStatus.OK, "1006", "회원 정보 수정에 성공했습니다."),
    PASSWORD_UPDATE(HttpStatus.OK, "1007", "비밀번호 수정에 성공했습니다."),
    USER_DELETED(HttpStatus.OK, "1008", "회원 탈퇴가 완료되었습니다."),

    STORE_CREATED(HttpStatus.CREATED, "1009", "가게가 성공적으로 생성되었습니다."),
    STORE_OWNER_FETCHED(HttpStatus.OK, "1010", "본인 가게 정보가 조회되었습니다."),
    STORE_SEARCH_FETCHED(HttpStatus.OK, "1011", "가게 검색이 성공적으로 완료되었습니다."),
    STORE_DETAIL_FETCHED(HttpStatus.OK, "1012", "가게 상세 정보가 조회되었습니다."),
    STORE_UPDATED(HttpStatus.OK, "1013", "가게 정보 수정이 완료되었습니다."),
    STORE_DELETED(HttpStatus.OK, "1014", "가게 삭제가 완료되었습니다."),

    MENU_CREATED(HttpStatus.CREATED, "1015", "메뉴가 성공적으로 생성되었습니다."),
    MENU_UPDATED(HttpStatus.OK, "1016", "메뉴가 성공적으로 수정되었습니다."),
    MENU_DELETED(HttpStatus.OK, "1017", "메뉴가 성공적으로 삭제되었습니다."),

    CART_ITEM_ADDED(HttpStatus.CREATED, "1018", "장바구니에 메뉴가 추가되었습니다."),
    CART_ITEM_FETCHED(HttpStatus.OK, "1019", "장바구니의 메뉴가 성공적으로 조회되었습니다."),
    CART_ITEM_UPDATED(HttpStatus.OK, "1020", "장바구니에서 선택한 메뉴의 수량이 변경되었습니다."),
    CART_CLEARED(HttpStatus.OK, "1021", "장바구니가 비워졌습니다."),

    ORDER_CREATED(HttpStatus.CREATED, "1022", "주문이 성공적으로 생성되었습니다."),
    STORE_ORDERS_FETCHED(HttpStatus.OK, "1023", "가게의 주문 목록이 성공적으로 조회되었습니다."),
    USER_ORDERS_FETCHED(HttpStatus.OK, "1024", "사용자의 주문 목록이 성공적으로 조회되었습니다."),
    ORDER_STATUS_UPDATED(HttpStatus.OK, "1025", "주문 상태가 성공적으로 변경되었습니다."),

    REVIEW_CREATED(HttpStatus.CREATED, "1026", "리뷰가 성공적으로 작성되었습니다."),
    REVIEW_FETCHED(HttpStatus.OK, "1027", "리뷰 목록이 성공적으로 조회되었습니다."),
    REVIEW_UPDATED(HttpStatus.OK, "1028", "리뷰가 성공적으로 수정되었습니다."),
    REVIEW_DELETED(HttpStatus.OK, "1029", "리뷰가 성공적으로 삭제되었습니다."),

    COMMENT_CREATED(HttpStatus.CREATED, "1030", "댓글이 성공적으로 작성되었습니다."),
    COMMENT_UPDATED(HttpStatus.OK, "1031", "댓글이 성공적으로 수정되었습니다."),
    COMMENT_DELETED(HttpStatus.OK, "1032", "댓글이 성공적으로 삭제되었습니다.");

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