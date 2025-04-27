package com.example.toanyone.domain.menu.entity;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false,unique = true, length = 10)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    public Menu(Store store, MenuDto.Request request) {
        this.store = store;
        this.name = request.getName();
        this.description =request.getDescription();
        this.price = request.getPrice();
        this.mainCategory = MainCategory.of(request.getMainCategory());
        this.subCategory = SubCategory.of(request.getSubCategory());
    }

    public void setMenu(MenuDto.Request request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.price = request.getPrice();
        this.mainCategory = MainCategory.of(request.getMainCategory());
        this.subCategory = SubCategory.of(request.getSubCategory());
    }

}
