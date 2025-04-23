package com.example.toanyone.domain.user.dto;

import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    public static class Get {

        private UserRole role;
        private String name;
        private String nickname;
        private String email;
        private String phone;
        private String address;
        private Gender gender;
        private Integer age;

    }

    @Getter
    public static class Complete {

        private String message;

        public Complete(String message) {
            this.message = message;
        }

    }
}
