package com.example.toanyone.domain.store.controller;


import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.service.StoreService;
import com.example.toanyone.global.common.annotation.Auth;
import com.example.toanyone.global.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<StoreResponseDto.Complete> createStore(@Auth AuthUser authUser,
                                                                 @Valid @RequestBody StoreRequestDto.Create dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(authUser.getId(), dto));
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

}

