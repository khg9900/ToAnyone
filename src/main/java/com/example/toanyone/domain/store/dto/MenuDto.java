package com.example.toanyone.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuDto {
    private Long menuId;
    private String menuName;
    private Integer price;
    private String description;
    //Todo category, type enum 추가
}
