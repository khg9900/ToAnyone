package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.user.dto.UserResponseDto.Complete;
import com.example.toanyone.global.auth.dto.AuthUser;
import jakarta.validation.Valid;
import com.example.toanyone.global.auth.dto.AuthUser;

import java.util.List;

public interface StoreService {

    void createStore(Long ownerId, StoreRequestDto.Create dto);

    List<StoreResponseDto.GetAll> getStoresByOwner(Long id);

    List<StoreResponseDto.GetAll> getStoresByName(String keyword);

    StoreResponseDto.GetById getStoreById(Long storeId);

    void updateStore(AuthUser authUser, Long storeId, StoreRequestDto.Update dto);

    void deleteStore(AuthUser authUser, Long storeId, StoreRequestDto.Delete dto);
}
