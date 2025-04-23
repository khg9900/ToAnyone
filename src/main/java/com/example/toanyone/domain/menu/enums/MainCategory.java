package com.example.toanyone.domain.menu.enums;

import java.util.Arrays;

public enum MainCategory {
    KOREAN,
    WESTERN,
    CHINESE,
    ETC;

    public static MainCategory of(String mainCategory) {
        return Arrays.stream(MainCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(mainCategory))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 메인 카테고리"));
    }
}
