package com.example.toanyone.domain.user.entity;

import com.example.toanyone.domain.user.enums.Gender;
import com.example.toanyone.domain.user.enums.UserRole;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.Getter;

@Getter
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    private Integer age;

}
