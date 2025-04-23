package com.example.toanyone.domain.cart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequestDto {

    @NotBlank(message = "메뉴 ID 입력은 필수입니다")
    private Long menuId;

    @NotBlank(message = "메뉴 수량 입력은 필수입니다")
    private Integer quantity;

}
