package com.example.toanyone.global.common.error;

import com.example.toanyone.global.common.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final BaseErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getReasonHttpStatus().getMessage();
    }

    public String getCode() {
        return errorCode.getReasonHttpStatus().getCode();
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getReasonHttpStatus().getHttpStatus();
    }
}
