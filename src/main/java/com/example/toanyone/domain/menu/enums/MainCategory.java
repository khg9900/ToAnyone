package com.example.toanyone.domain.menu.enums;

import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;

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
                .orElseThrow(() -> new ApiException(ErrorStatus.INVALID_MAIN_CATEGORY));
    }
}
