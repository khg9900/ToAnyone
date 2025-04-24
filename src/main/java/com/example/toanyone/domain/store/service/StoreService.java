package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.global.auth.dto.AuthUser;
import jakarta.validation.Valid;

import java.util.List;

public interface StoreService {

    StoreResponseDto.Complete createStore(Long ownerId, StoreRequestDto.Create dto);

    List<StoreResponseDto.GetAll> getStoresByOwner(Long id);

    List<StoreResponseDto.GetAll> getStoresByName(String keyword);

    StoreResponseDto.Complete updateStore(AuthUser authUser, Long storeId, StoreRequestDto.@Valid Update dto);
}
