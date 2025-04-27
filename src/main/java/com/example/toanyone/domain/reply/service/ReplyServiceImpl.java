package com.example.toanyone.domain.reply.service;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 생성
     */
    @Override
    @Transactional
    public ReplyResponseDto createReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto) {

        // 리뷰 및 사장님 유저 검증
        validateReviewAndUser(storeId, reviewId, authUser, false);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));

        if (review.getReply() != null) {
            throw new ApiException(ErrorStatus.REPLY_ALREADY_EXISTS);
        }

        // 로그인한 사장님 가져오기
        User owner = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        // 댓글 작성자는 사장님(owner)
        Reply reply = new Reply(review, owner, requestDto.getContent());
        replyRepository.save(reply);

        return new ReplyResponseDto("댓글이 등록되었습니다.");
    }

    /**
     * 댓글 수정
     */
    @Override
    @Transactional
    public ReplyResponseDto updateReply(Long storeId, Long reviewId, AuthUser authUser, ReplyRequestDto requestDto) {

        // 리뷰 및 사장님 유저 검증
        validateReviewAndUser(storeId, reviewId, authUser, true);


        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));
        Reply reply = review.getReply();
        reply.updateContent(requestDto.getContent());

        return new ReplyResponseDto("댓글이 수정되었습니다.");
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    @Override
    public ReplyResponseDto deleteReply(Long storeId, Long reviewId, AuthUser authUser) {

        // 리뷰 및 사장님 유저 검증
        validateReviewAndUser(storeId, reviewId, authUser, true);


        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));
        Reply reply = review.getReply();
        reply.softDelete();

        return new ReplyResponseDto("댓글이 정상적으로 삭제되었습니다.");
    }

    /**
     * 공통 검증 로직
     * 댓글을 수정하거나 삭제할 때 필요한 검증
     */
    private void validateReviewAndUser(Long storeId, Long reviewId, AuthUser authUser, boolean checkReplyOwner) {
        // 리뷰 존재 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));

        // 해당 가게 리뷰인지 검증
        if (!review.getStore().getId().equals(storeId)) {
            throw new ApiException(ErrorStatus.REVIEW_STORE_MISMATCH);
        }

        // 사장님 유저 검증
        User owner = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        if (owner.getUserRole() == null || !owner.getUserRole().equals(UserRole.OWNER)) {
            throw new ApiException(ErrorStatus.REVIEW_ACCESS_DENIED);
        }

        // 댓글이 존재하는지 검증
        if (checkReplyOwner) {
            Reply reply = review.getReply();
            if (reply == null) {
                throw new ApiException(ErrorStatus.REPLY_NOT_FOUND);
            }

            // 댓글 작성자 본인 확인
            if (!reply.getOwner().getId().equals(owner.getId())) {
                throw new ApiException(ErrorStatus.REVIEW_ACCESS_DENIED);
            }
        }
    }
}
