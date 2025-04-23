package com.example.toanyone.domain.store.entity;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name= "store")
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String name;
    private String address;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Integer deliveryFee;
    private String notice;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean deleted;
    private LocalDateTime deletedAt;

    //Todo BaseEntity 확인하기

}
