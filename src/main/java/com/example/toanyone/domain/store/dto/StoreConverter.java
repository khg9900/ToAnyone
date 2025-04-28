package com.example.toanyone.domain.store.dto;

import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StoreConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalTime toLocalTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ApiException(ErrorStatus.INVALID_TIME_RANGE);
        }
    }

    public static Status toStatus(String statusStr) {
        return Status.of(statusStr);
    }

}

