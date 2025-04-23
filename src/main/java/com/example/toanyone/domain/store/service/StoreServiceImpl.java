package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    public StoreRepository storeRepository;
    public UserRepository userRepository;

    @Override
    public StoreResponseDto.Complete createStore(StoreRequestDto.Create dto) {

        Store newStore = new Store(dto);
        storeRepository.save(newStore);

        return new StoreResponseDto.Complete("가게가 생성되었습니다.");
    }
}
