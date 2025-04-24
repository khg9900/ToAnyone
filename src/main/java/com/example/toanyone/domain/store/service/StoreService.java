package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;

import java.util.List;

public interface StoreService {

    StoreResponseDto.Complete createStore(Long ownerId, StoreRequestDto.Create dto);

    List<StoreResponseDto.GetAll> getStoresByOwner(Long id);
}
