package com.example.toanyone.domain.menu.dto;

import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MenuDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "메뉴 이름 입력은 입력은 필수입니다")
        private String name;

        @NotBlank(message = "메뉴 설명 입력은 필수입니다")
        private String description;

        @NotBlank(message = "메뉴 가격 입력은 필수입니다")
        private int price;

        @NotBlank(message = "메뉴 메인 카테고리 입력은 필수입니다")
        private MainCategory mainCategory;

        @NotBlank(message = "메뉴 세부 카테고리 입력은 필수입니다")
        private SubCategory subCategory;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String name;
        private String description;
        private int price;
    }




}
