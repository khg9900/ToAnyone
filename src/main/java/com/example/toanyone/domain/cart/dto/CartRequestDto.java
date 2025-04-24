package com.example.toanyone.domain.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequestDto {

    @NotNull(message = "가게 ID 입력은 필수입니다")
    private Long storeId;

    @NotNull(message = "메뉴 ID 입력은 필수입니다")
    private Long menuId;

    @NotNull(message = "메뉴 수량 입력은 필수입니다")
    private Integer quantity;

}
