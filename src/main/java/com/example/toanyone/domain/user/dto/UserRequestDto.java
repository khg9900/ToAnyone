package com.example.toanyone.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Update {

        @NotBlank
        private String nickname;
        @NotBlank
        private String address;
    }

    @Getter
    @AllArgsConstructor
    public static class ChangePassword {

        @NotBlank
        private String oldPassword;
        @NotBlank
        @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다.")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "영문 대소문자, 숫자, 특수문자를 각각 최소 1자 이상 포함해야 합니다."
        )
        private String newPassword;
    }

    @Getter
    @AllArgsConstructor
    public static class Delete {

        @NotBlank
        private String password;
    }

}
