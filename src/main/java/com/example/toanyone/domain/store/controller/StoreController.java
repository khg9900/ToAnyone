package com.example.toanyone.domain.store.controller;


import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.service.StoreService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {

    final StoreService storeService;

    /**
     * 가게 생성
     * @param authUser 로그인된 유저(OWNER)
     * @param dto 가게생성시 필요한 정보
     * @return 생성 완료 메세지
     */
    @PostMapping("/owner/stores")
    public ResponseEntity<ApiResponse<Void>> createStore(
        @Auth AuthUser authUser,
        @Valid @RequestBody StoreRequestDto.Create dto
    ) {
        storeService.createStore(authUser.getId(), dto);
        return ApiResponse.onSuccess(SuccessStatus.STORE_CREATED);
    }

    /**
     * 본인(OWNER) 가게 조회
     * @param authUser 로그인 된 유저(OWNER)
     * @return Store List
     */
    @GetMapping("/owner/stores")
    public ResponseEntity<ApiResponse<List<StoreResponseDto.GetAll>>> getStoresByOwner(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(SuccessStatus.STORE_OWNER_FETCHED, storeService.getStoresByOwner(authUser.getId()));
    }

    /**
     * 가게명 키워드 검색 조회
     * @param keyword 검색단어
     * @return Store List
     */
    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<List<StoreResponseDto.GetAll>>> getStoresByKeyword(@RequestParam("keyword") String keyword) {
        return ApiResponse.onSuccess(SuccessStatus.STORE_SEARCH_FETCHED, storeService.getStoresByName(keyword));
    }

    /**
     * 가게 단건 조회
     * @param storeId 가게Id
     * @return 가게 정보 및 메뉴 정보 리스트
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto.GetById>> getStoreById(@PathVariable Long storeId) {
        return ApiResponse.onSuccess(SuccessStatus.STORE_DETAIL_FETCHED, storeService.getStoreById(storeId));
    }

    /**
     * 가게 정보 수정
     * @param authUser 로그인 유저 정보
     * @param storeId 수정할 가게 Id
     * @param dto 수정 필드 내용
     * @return
     */
    @PatchMapping("/owner/stores/{storeId}")
    public ResponseEntity<ApiResponse<Void>> updateStore(
        @Auth AuthUser authUser,
        @PathVariable Long storeId,
        @Valid @RequestBody StoreRequestDto.Update dto
    ) {
        storeService.updateStore(authUser, storeId, dto);
        return ApiResponse.onSuccess(SuccessStatus.STORE_UPDATED);
    }

    /**
     * 가게 폐업처리(soft delete)
     * @param authUser 요청 유저
     * @param storeId 폐업 처리할 store Id
     * @param dto 비밀번호 검증
     * @return
     */
    @DeleteMapping("/owner/stores/{storeId}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(
        @Auth AuthUser authUser,
        @PathVariable Long storeId,
        @Valid @RequestBody StoreRequestDto.Delete dto
    ) {
        storeService.deleteStore(authUser, storeId, dto);
        return ApiResponse.onSuccess(SuccessStatus.STORE_DELETED);
    }
}

