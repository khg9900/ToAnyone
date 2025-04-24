package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    public final StoreRepository storeRepository;
    public final UserRepository userRepository;

    /**
     * 가게 생성(Service)
     */
    @Override
    public StoreResponseDto.Complete createStore(Long ownerId, StoreRequestDto.Create dto) {

        User user = userRepository.findById(ownerId).orElseThrow(
                () -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        if (user.getUserRole() != UserRole.OWNER) {
            throw new ApiException(ErrorStatus.STORE_NO_PERMISSION);}

        int storeCount = storeRepository.countByUserIdAndDeletedFalse(ownerId);
        if (storeCount >= 3) {
            throw new ApiException(ErrorStatus.STORE_MAX_LIMIT_EXCEEDED);}

        if (storeRepository.existsByName(dto.getName())) {
            throw new ApiException(ErrorStatus.STORE_ALREADY_EXISTS);}

        Store newStore = new Store(user, dto);
        storeRepository.save(newStore);

        return new StoreResponseDto.Complete("가게가 생성되었습니다.");
    }

    /**
     * 본인(OWNER) 가게 조회
     */
    @Override
    public List<StoreResponseDto.GetAll> getStoresByOwner(Long ownerId) {

        int storeCount = storeRepository.countByUserIdAndDeletedFalse(ownerId);
        if(storeCount == 0) {
            throw new ApiException(ErrorStatus.STORE_NOT_FOUND);}

        List<Store> stores = storeRepository.findByUserIdAndDeletedFalse(ownerId);
        List<StoreResponseDto.GetAll> result = new ArrayList<>();

        for (Store store : stores) {
            result.add(new StoreResponseDto.GetAll(store.getId(), store.getName()));
        }

        return result;
    }

    /**
     * 가게명 키워드 검색 조회
     */
    @Override
    public List<StoreResponseDto.GetAll> getStoresByName(String keyword) {

        List<Store> stores = storeRepository.findByNameContainingAndDeletedFalse(keyword);

        if(stores.isEmpty()) {
            throw new ApiException(ErrorStatus.STORE_SEARCH_NO_MATCH);}

        List<StoreResponseDto.GetAll> result = new ArrayList<>();
        for (Store store : stores) {
            result.add(new StoreResponseDto.GetAll(store.getId(), store.getName()));
        }

        return result;
    }

    /**
     * 가게 단건 조회
     */
    @Override
    public StoreResponseDto.GetById getStoreById(Long storeId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(store.getDeleted()) {
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);
        }

        return new StoreResponseDto.GetById(store);
    }


}
