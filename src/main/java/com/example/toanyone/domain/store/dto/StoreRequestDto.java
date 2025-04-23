package com.example.toanyone.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class StoreRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        @NotBlank
        private String name;
        @NotBlank
        private String address;
        @NotBlank
        private LocalDateTime openTime;
        @NotBlank
        private LocalDateTime closeTime;
        @NotBlank
        private Integer deliveryFee;
        @NotBlank
        private Integer minOrderPrice;
        private String notice;
        @NotBlank
        private String state;
        @NotBlank
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
        private String phone;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Update {
        private LocalDateTime openTime;
        private LocalDateTime closeTime;
        private Integer deliveryFee;
        private Integer minOrderPrice;
        private String notice;
        private String state;
    }

    @Getter
    @AllArgsConstructor
    public static class Delete {
        @NotBlank
        private String password;
    }

}
