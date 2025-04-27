package com.example.toanyone.domain.reply.service;

import com.example.toanyone.domain.reply.dto.ReplyRequestDto;
import com.example.toanyone.domain.reply.dto.ReplyResponseDto;
import com.example.toanyone.global.auth.dto.AuthUser;
import jakarta.validation.Valid;

public interface ReplyService {

    ReplyResponseDto createReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto);

    ReplyResponseDto updateReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto);
}
