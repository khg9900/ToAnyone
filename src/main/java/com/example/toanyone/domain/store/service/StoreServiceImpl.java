package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.dto.UserResponseDto.Complete;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import com.example.toanyone.global.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    public final StoreRepository storeRepository;
    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 가게 생성
     */
    @Override
    public void createStore(Long ownerId, StoreRequestDto.Create dto) {

        // DB 접근 검증
        int storeCount = storeRepository.countByUserIdAndDeletedFalse(ownerId);
        if (storeCount >= 3) {
            throw new ApiException(ErrorStatus.STORE_MAX_LIMIT_EXCEEDED);}

        if (storeRepository.existsByName(dto.getName())) {
            throw new ApiException(ErrorStatus.STORE_ALREADY_EXISTS);}

        User user = userRepository.findById(ownerId).orElseThrow(
                () -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Store newStore = new Store(user, dto);
        storeRepository.save(newStore);
    }

    /**
     * 본인(OWNER) 가게 조회
     */
    @Override
    public List<StoreResponseDto.GetAll> getStoresByOwner(Long ownerId) {

        boolean exists = storeRepository.existsByUserIdAndDeletedFalse(ownerId);
        if(!exists) {
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
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);}

        return new StoreResponseDto.GetById(store);
    }

    /**
     * 가게 정보 수정
     */
    @Override
    @Transactional
    public void updateStore(AuthUser authUser, Long storeId, StoreRequestDto.Update dto) {

        // DB 접근 검증
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(!authUser.getId().equals(store.getUser().getId())) {
            throw new ApiException(ErrorStatus.STORE_FORBIDDEN);}

        if(store.getDeleted()) {
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);}

        store.update(dto);
    }

    /**
     * 가게 폐업처리(soft delete)
     */
    @Override
    @Transactional
    public void deleteStore(AuthUser authUser, Long storeId, StoreRequestDto.Delete dto) {
        User user = userRepository.findByIdOrElseThrow(authUser.getId());

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus.INVALID_PASSWORD);};

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(!authUser.getId().equals(store.getUser().getId())) {
            throw new ApiException(ErrorStatus.STORE_FORBIDDEN);}

        if(store.getDeleted()) {
            throw new ApiException(ErrorStatus.STORE_ALREADY_DELETED);}

        store.softDelete();
    }


}
