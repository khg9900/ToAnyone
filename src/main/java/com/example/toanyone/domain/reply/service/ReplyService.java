package com.example.toanyone.domain.reply.service;

import com.example.toanyone.domain.reply.dto.ReplyRequestDto;
import com.example.toanyone.global.auth.dto.AuthUser;

public interface ReplyService {

    void createReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto);

    void updateReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto);

    void deleteReply(Long storeId, Long reviewId, AuthUser authUser);
}
