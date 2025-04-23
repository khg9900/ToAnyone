package com.example.toanyone.domain.user.entity;

import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    private Integer age;

    public User(String email, String password, String name, UserRole role, String nickname,
        String phone, String address, Gender gender, LocalDate birth, int age) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.userRole = role;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birth = birth;
        this.age = age;
    }
}
