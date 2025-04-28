package com.example.toanyone.domain.reply.controller;

import com.example.toanyone.domain.reply.dto.ReplyRequestDto;
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
    public ResponseEntity<ApiResponse<Void>> replyCreate(
        @PathVariable Long storeId,
        @PathVariable Long reviewId,
        @Auth AuthUser authUser,
        @Valid @RequestBody ReplyRequestDto requestDto
    ) {
        replyService.createReply(storeId, reviewId, authUser, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_CREATED);
    }

    /**
     * 댓글 수정
     * */
    @PatchMapping("/{storeId}/reviews/{reviewId}/reply")
    public ResponseEntity<ApiResponse<Void>> replyUpdate(
        @PathVariable Long storeId,
        @PathVariable Long reviewId,
        @Auth AuthUser authUser,
        @Valid @RequestBody ReplyRequestDto requestDto
    ) {
        replyService.updateReply(storeId, reviewId, authUser, requestDto);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_UPDATED);
    }

    /**
     *
     * 댓글 삭제
     */
    @DeleteMapping("/{storeId}/reviews/{reviewId}/reply")
    public ResponseEntity<ApiResponse<Void>> replyDelete(
        @PathVariable Long storeId,
        @PathVariable Long reviewId,
        @Auth AuthUser authUser
    ) {
        replyService.deleteReply(storeId,reviewId,authUser);
        return ApiResponse.onSuccess(SuccessStatus.COMMENT_DELETED);
    }
}
