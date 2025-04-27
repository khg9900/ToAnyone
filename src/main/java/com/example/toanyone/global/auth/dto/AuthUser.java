package com.example.toanyone.global.auth.dto;

import com.example.toanyone.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final String userRole;

    public AuthUser(Long id, String email, String userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}
