package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    public final StoreRepository storeRepository;
    public final UserRepository userRepository;

    @Override
    public StoreResponseDto.Complete createStore(Long ownerId, StoreRequestDto.Create dto) {

        User user = userRepository.findById(ownerId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 유저입니다."));

        if (user.getUserRole() != UserRole.OWNER) {
            throw new RuntimeException("가게 생성 권한이 없습니다.");}

        int storeCount = storeRepository.countByUserIdAndDeletedFalse(ownerId);
        if (storeCount >= 3) {
            throw new RuntimeException("가게는 최대 3개까지 등록 가능합니다.");}

        if (storeRepository.existsByName(dto.getName())) {
            throw new RuntimeException("이미 존재하는 가게이름입니다.");}

        Store newStore = new Store(user, dto);
        storeRepository.save(newStore);

        return new StoreResponseDto.Complete("가게가 생성되었습니다.");
    }
}
