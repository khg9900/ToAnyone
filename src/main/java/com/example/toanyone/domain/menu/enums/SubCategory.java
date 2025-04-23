package com.example.toanyone.domain.menu.enums;

import java.util.Arrays;

public enum SubCategory {
    MAIN,
    SIDE,
    DRINK;

    public static SubCategory of(String subCategory) {
        return Arrays.stream(SubCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(subCategory))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 세부 카테고리"));
    }
}
