package com.example.toanyone.domain.menu.service;

import com.example.toanyone.domain.menu.dto.MenuDto;
import com.example.toanyone.domain.menu.enums.MainCategory;
import com.example.toanyone.domain.menu.enums.SubCategory;
import org.springframework.stereotype.Service;

@Service
public interface MenuService {

    MenuDto.Response createMenu(Integer storeId, String name, String description,
                                Integer price, MainCategory mainCategory, SubCategory subCategory);

}