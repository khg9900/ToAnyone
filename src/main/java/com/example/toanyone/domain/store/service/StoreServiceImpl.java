package com.example.toanyone.domain.store.service;

import com.example.toanyone.domain.menu.repository.MenuRepository;
import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.store.enums.Status;
import com.example.toanyone.domain.store.repository.StoreRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import com.example.toanyone.global.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public StoreResponseDto.Complete createStore(Long ownerId, StoreRequestDto.Create dto) {

        // Dto 데이터 타입 변환 검증
        Status status = Status.of(dto.getStatus());
        LocalTime openTime;
        LocalTime closeTime;

        try {
            openTime = LocalTime.parse(dto.getOpenTime(), DateTimeFormatter.ofPattern("HH:mm"));
            closeTime = LocalTime.parse(dto.getCloseTime(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new ApiException(ErrorStatus.INVALID_TIME_RANGE);
        }

        // DB 접근 검증
        int storeCount = storeRepository.countByUserIdAndDeletedFalse(ownerId);
        if (storeCount >= 3) {
            throw new ApiException(ErrorStatus.STORE_MAX_LIMIT_EXCEEDED);}

        if (storeRepository.existsByName(dto.getName())) {
            throw new ApiException(ErrorStatus.STORE_ALREADY_EXISTS);}

        User user = userRepository.findById(ownerId).orElseThrow(
                () -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Store newStore = new Store(user, dto, status, openTime, closeTime);
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

    /**
     * 가게 정보 수정
     */
    @Override
    @Transactional
    public StoreResponseDto.Complete updateStore(AuthUser authUser, Long storeId, StoreRequestDto.Update dto) {

        // Dto 데이터 타입 변환 검증
        Status status = null;
        LocalTime openTime = null;
        LocalTime closeTime = null;

        if(dto.getStatus() != null) {
            status = Status.of(dto.getStatus());
        }

        if(dto.getOpenTime() != null) {
            try {
                openTime = LocalTime.parse(dto.getOpenTime(), DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                throw new ApiException(ErrorStatus.INVALID_TIME_RANGE);
            }
        }

        if(dto.getCloseTime() != null) {
            try {
                closeTime = LocalTime.parse(dto.getCloseTime(), DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                throw new ApiException(ErrorStatus.INVALID_TIME_RANGE);
            }
        }

        // DB 접근 검증
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(!authUser.getId().equals(store.getUser().getId())) {
            throw new ApiException(ErrorStatus.STORE_FORBIDDEN);}

        if(store.getDeleted()) {
            throw new ApiException(ErrorStatus.STORE_SHUT_DOWN);}

        store.update(dto, status, openTime, closeTime);

        return new StoreResponseDto.Complete("정보가 수정되었습니다.");
    }

    /**
     * 가게 폐업처리(soft delete)
     */
    @Override
    @Transactional
    public StoreResponseDto.Complete deleteStore(AuthUser authUser, Long storeId, StoreRequestDto.Delete dto) {
        Optional<User> user = userRepository.findById(authUser.getId());

        if(!passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
            throw new ApiException(ErrorStatus.INVALID_PASSWORD);};

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if(!authUser.getId().equals(store.getUser().getId())) {
            throw new ApiException(ErrorStatus.STORE_FORBIDDEN);
        }

        if(store.getDeleted()) {
            throw new ApiException(ErrorStatus.STORE_ALREADY_DELETED);}

        store.softDelete();
        return new StoreResponseDto.Complete("가게가 폐업 처리되었습니다.");
    }


}
