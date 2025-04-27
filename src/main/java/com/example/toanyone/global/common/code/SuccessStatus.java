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
    CREATED(HttpStatus.CREATED, "1001", "생성 완료"),

    // 2000: 리뷰 성공 코드
    REVIEW_CREATED(HttpStatus.CREATED, "2001", "리뷰가 작성되었습니다."),
    REVIEW_UPDATED(HttpStatus.OK, "2002", "리뷰가 성공적으로 수정되었습니다."),
    REVIEW_DELETED(HttpStatus.OK, "2003", "리뷰가 성공적으로 삭제되었습니다."),
    REVIEW_FETCHED(HttpStatus.OK, "2004", "리뷰 목록이 성공적으로 조회되었습니다.");

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