package com.example.toanyone.global.common.code;

import com.example.toanyone.global.common.dto.ErrorReasonDto;

public interface BaseErrorCode {
    ErrorReasonDto getReason();
    ErrorReasonDto getReasonHttpStatus();
}
