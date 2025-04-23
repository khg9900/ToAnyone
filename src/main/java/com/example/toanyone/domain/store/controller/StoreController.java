package com.example.toanyone.domain.store.controller;


import com.example.toanyone.domain.store.dto.StoreRequestDto;
import com.example.toanyone.domain.store.dto.StoreResponseDto;
import com.example.toanyone.domain.store.service.StoreService;
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

    @PostMapping("/owner/stores")
    public ResponseEntity<StoreResponseDto.Complete> createStore(@RequestBody StoreRequestDto.Create dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(dto));
    }

}
