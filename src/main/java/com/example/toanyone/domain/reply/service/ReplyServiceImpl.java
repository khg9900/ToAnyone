package com.example.toanyone.domain.reply.service;

import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.dto.ReplyRequestDto;
import com.example.toanyone.domain.reply.dto.ReplyResponseDto;
import com.example.toanyone.domain.reply.entity.Reply;
import com.example.toanyone.domain.reply.repository.ReplyRepository;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.repository.ReviewRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.enums.UserRole;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public ReplyResponseDto createReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto) {

        // 리뷰 존재 확인
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));

        // 해당 가게 리뷰인지 검증
        if (!review.getStore().getId().equals(storeId)){
            throw new ApiException(ErrorStatus.REVIEW_STORE_MISMATCH);
        }

        // 사장님 유저 검증
        User owner = userRepository.findById(authUser.getId()).orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        if (!owner.getUserRole().equals(UserRole.OWNER)){
            throw new ApiException(ErrorStatus.REVIEW_ACCESS_DENIED);
        }

        // 댓글 존재 여부 확인
        if (review.getReply() != null) {
            throw new ApiException(ErrorStatus.REPLY_ALREADY_EXISTS);
        }

        // 댓글 생성 및 저장
        Reply reply = new Reply(review, owner, requestDto.getContent());
        replyRepository.save(reply);

        return new ReplyResponseDto("댓글이 등록되었습니다.");
    }
}
