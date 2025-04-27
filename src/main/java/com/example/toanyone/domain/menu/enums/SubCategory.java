package com.example.toanyone.domain.menu.enums;

import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;

import java.util.Arrays;

public enum SubCategory {
    MAIN,
    SIDE,
    DRINK;

    public static SubCategory of(String subCategory) {
        return Arrays.stream(SubCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(subCategory))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorStatus.INVALID_SUB_CATEGORY));
    }
}
