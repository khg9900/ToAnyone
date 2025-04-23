package com.example.toanyone.domain.store.entity;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
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

    private Integer minOrderPrice;

    @Column(columnDefinition = "TEXT")
    private String notice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String phone;

    public Store(StoreRequestDto.Create dto) {
        this.user = user; //todo 어떻게 받아오지?, 이때 owner의 가게수 확인?
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.deliveryFee = dto.getDeliveryFee();
        this.minOrderPrice = dto.getMinOrderPrice();
        this.notice = dto.getNotice();
        this.status = dto.getStatus();
        this.phone = dto.getPhone();

    }



}
