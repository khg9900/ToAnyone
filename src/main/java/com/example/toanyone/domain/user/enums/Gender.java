package com.example.toanyone.domain.user.enums;

import java.util.Arrays;

public enum Gender {
    MALE, FEMALE, OTHER;

    public static Gender of(String gender) {
        return Arrays.stream(Gender.values())
            .filter(r -> r.name().equalsIgnoreCase(gender))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("유효하지 않은 Gender"));
    }
}
