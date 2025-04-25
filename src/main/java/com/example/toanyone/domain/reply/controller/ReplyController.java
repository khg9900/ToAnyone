package com.example.toanyone.domain.reply.controller;

import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.dto.ReplyRequestDto;
import com.example.toanyone.domain.reply.dto.ReplyResponseDto;
import com.example.toanyone.domain.reply.service.ReplyService;
import com.example.toanyone.global.auth.annotation.Auth;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.SuccessStatus;
import com.example.toanyone.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/stores")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글 작성
     * */
    @PostMapping("/{storeId}/reviews/{reviewId}/reply")
    public ResponseEntity<ApiResponse<ReplyResponseDto>> replyCreate(@PathVariable Long storeId,
                                                                     @PathVariable Long reviewId,
                                                                     @Auth AuthUser authUser,
                                                                     @Valid @RequestBody ReplyRequestDto requestDto) {
        ReplyResponseDto responseDto = replyService.createReply(storeId, reviewId, authUser, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.OK, responseDto);
    }

    /**
     * 댓글 수정
     * */
    @PatchMapping("/{storeId}/reviews/{reviewId}/reply")
    public ResponseEntity<ApiResponse<ReplyResponseDto>> replyUpdate(@PathVariable Long storeId,
                                                                     @PathVariable Long reviewId,
                                                                     @Auth AuthUser authUser,
                                                                     @Valid @RequestBody ReplyRequestDto requestDto) {
        ReplyResponseDto responseDto = replyService.updateReply(storeId, reviewId, authUser, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.OK, responseDto);
    }
}
