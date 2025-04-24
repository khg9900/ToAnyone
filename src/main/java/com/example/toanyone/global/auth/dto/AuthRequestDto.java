package com.example.toanyone.global.auth.dto;

import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class  AuthRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Signup {

        @NotBlank
        private String role;
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다.")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "영문 대소문자, 숫자, 특수문자를 각각 최소 1자 이상 포함해야 합니다."
        )
        private String password;
        @NotBlank
        @Pattern(
            regexp = "^01[0-1,6-9]-\\d{3,4}-\\d{4}$",
            message = "올바른 휴대폰 번호 형식이 아닙니다. (예: 010-1234-5678)"
        )
        private String phone;
        @NotBlank
        private String address;
        @NotBlank
        private String gender;
        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
        private String birth;

    }

    @Getter
    @AllArgsConstructor
    public static class Signin {

        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;
    }

}