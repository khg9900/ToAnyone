package com.example.toanyone.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponseDto {

    @Getter
    public static class CreateToken {

        private final String bearerToken;

        public CreateToken(String bearerToken) {
            this.bearerToken = bearerToken;
        }

    }

    @Getter
    public static class Signout {

        private String message;

        public Signout(String message) {
            this.message = message;
        }

    }

}
