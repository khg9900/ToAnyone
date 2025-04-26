package com.example.toanyone.global.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String refresh;

    private String expiration;

    public Refresh(Long userId, String refresh, String expiration) {
        this.userId = userId;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
