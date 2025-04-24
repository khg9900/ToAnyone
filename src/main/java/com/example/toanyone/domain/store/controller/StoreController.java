package com.example.toanyone.domain.store.controller;


import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.service.StoreService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

    final StoreService storeService;

    /**
     * 가게 생성
     * @param authUser 로그인된 유저
     * @param dto 가게생성시 필요한 정보
     * @return 생성 완료 메세지
     */
    @PostMapping("/owner/stores")
    public ResponseEntity<StoreResponseDto.Complete> createStore(@Auth AuthUser authUser,
                                                                 @Valid @RequestBody StoreRequestDto.Create dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(authUser.getId(), dto));
    }

}

