package com.example.toanyone.domain.store.controller;


import com.example.toanyone.domain.store.dto.StoreResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    @PostMapping("/owner/stores")
    public ResponseEntity<StoreResponseDto.Complete> createStore() {

        return ResponseEntity.status(HttpStatus.CREATED).body();
    }

}
