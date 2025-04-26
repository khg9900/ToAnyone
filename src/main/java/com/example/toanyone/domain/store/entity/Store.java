package com.example.toanyone.domain.store.entity;

import com.example.toanyone.domain.menu.entity.Menu;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private Integer deliveryFee;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(columnDefinition = "TEXT")
    private String notice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus = new ArrayList<>();



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id") // 이 컬럼 이름이 실제 DB랑 맞아야 함!
//    private User owner;

    // 고승표 추가
    public boolean isOpen() {
        return this.status == Status.OPEN;
    }

    public Store(User user, StoreRequestDto.Create dto, Status status, LocalTime openTime, LocalTime closeTime) {
        this.user = user;
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.deliveryFee = dto.getDeliveryFee();
        this.minOrderPrice = dto.getMinOrderPrice();
        this.notice = dto.getNotice();
        this.status = status;
        this.phone = dto.getPhone();
    }

    public void update(StoreRequestDto.Update dto, Status status, LocalTime openTime, LocalTime closeTime) {
        if (dto.getOpenTime() != null) this.openTime = openTime;
        if (dto.getCloseTime() != null) this.closeTime = closeTime;
        if (dto.getDeliveryFee() != null) this.deliveryFee = dto.getDeliveryFee();
        if (dto.getMinOrderPrice() != null) this.minOrderPrice = dto.getMinOrderPrice();
        if (dto.getNotice() != null) this.notice = dto.getNotice();
        if (dto.getStatus() != null) this.status = status;
    }

}