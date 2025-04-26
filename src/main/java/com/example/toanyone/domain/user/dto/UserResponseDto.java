package com.example.toanyone.domain.user.dto;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {

    @Getter
    @NoArgsConstructor
    public static class Get {

        private UserRole role;
        private String name;
        private String nickname;
        private String email;
        private String phone;
        private String address;
        private Gender gender;
        private Integer age;

        public Get(User user) {
            this.role = user.getUserRole();
            this.name = user.getUsername();
            this.nickname = user.getNickname();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.address = user.getAddress();
            this.gender = user.getGender();
            this.age = user.getAge();
        }
    }

    @Getter
    public static class Complete {

        private String message;

        public Complete(String message) {
            this.message = message;
        }

    }
}
