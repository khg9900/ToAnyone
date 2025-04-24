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
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<StoreResponseDto.Complete>> createStore(@Auth AuthUser authUser,
                                                                             @Valid @RequestBody StoreRequestDto.Create dto) {
        StoreResponseDto.Complete responseDto = storeService.createStore(authUser.getId(), dto);

        return ApiResponse.onSuccess(SuccessStatus.CREATED, responseDto);
    }

    /**
     * 본인(OWNER) 가게 조회
     * @param authUser 로그인 된 유저(OWNER)
     * @return Store List
     */
    @GetMapping("/owner/stores")
    public ResponseEntity<List<StoreResponseDto.GetAll>> getStoresByOwner(@Auth AuthUser authUser) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoresByOwner(authUser.getId()));
    }

    /**
     * 가게명 키워드 검색 조회
     * @param keyword 검색단어
     * @return Store List
     */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto.GetAll>> getStoresByKeyword(@RequestParam("keyword") String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoresByName(keyword));
    }

}

