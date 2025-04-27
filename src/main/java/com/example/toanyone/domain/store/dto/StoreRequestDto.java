package com.example.toanyone.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class StoreRequestDto {

    // 가게 생성
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        @NotBlank(message = "가게 이름 입력은 필수입니다.")
        private String name;

        @NotBlank(message = "가게 주소 입력은 필수입니다.")
        private String address;

        @NotBlank(message = "가게 오픈시간 입력은 필수입니다.")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "HH:mm 형식으로 입력해주세요.")
        private String openTime;

        @NotBlank(message = "가게 마감시간 입력은 필수입니다.")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "HH:mm 형식으로 입력해주세요.")
        private String closeTime;

        @NotNull(message = "배달비 입력은 필수입니다.")
        private Integer deliveryFee;

        @NotNull(message = "최소 주문 금액 입력은 필수입니다.")
        private Integer minOrderPrice;

        private String notice;

        @NotBlank(message = "가게 상태 입력은 필수입니다.")
        private String status;

        @NotBlank(message = "가게 번호 입력은 필수입니다.")
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호는 000-0000-0000 형식이어야 합니다.")
        private String phone;
    }

    // 가게 정보 수정
    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Update {
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "HH:mm 형식으로 입력해주세요.")
        private String openTime;

        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "HH:mm 형식으로 입력해주세요.")
        private String closeTime;

        private Integer deliveryFee;
        private Integer minOrderPrice;
        private String notice;
        private String status;
    }

    // 가게 폐업 처리
    @Getter
    @AllArgsConstructor
    public static class Delete {
        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;
    }

}
