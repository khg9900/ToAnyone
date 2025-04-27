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

    @Getter
    @AllArgsConstructor
    public static class Signout {
        private String message;
    }

    @Getter
    @AllArgsConstructor
    public static class Reissue {
        private String newAccessToken;
    }

}
