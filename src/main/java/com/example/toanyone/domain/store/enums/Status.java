package com.example.toanyone.domain.store.enums;

import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {
    OPEN, SOLD_OUT, TEMP_CLOSED, CLOSED;

    public static Status of(String status) {
        return Arrays.stream(Status.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorStatus.STORE_INVALID_STATUS));
    }
}