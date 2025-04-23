package com.example.toanyone.domain.store.entity;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name= "stores")
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime openTime;

    @Column(nullable = false)
    private LocalDateTime closeTime;

    @Column(nullable = false)
    private Integer deliveryFee;

    @Column(columnDefinition = "TEXT")
    private String notice;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

}
