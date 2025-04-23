package com.example.toanyone.domain.store.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {
    OPEN, SOLD_OUT, TEMP_CLOSED, CLOSED;

    public static Status of(String status) {
        return Arrays.stream(Status.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 상태입니다."));
    }
}
