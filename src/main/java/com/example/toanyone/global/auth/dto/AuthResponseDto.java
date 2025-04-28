package com.example.toanyone.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponseDto {

    @Getter
    @AllArgsConstructor
    public static class CreateToken {
        private final String accessToken;
        private final String refreshToken;
    }

}
