package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.entity.Reply;
import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.repository.ReviewRepository;
import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.auth.dto.AuthUser;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 생성
     * */
    @Override
    @Transactional
    public void createReview(Long storeId, Long orderId, AuthUser authUser, ReviewCreateRequestDto request){

        // 가게 일치 여부 검증
        Order order = orderRepository.findReviewableOrder(orderId, authUser.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_CREATE_FORBIDDEN));

        if (!storeId.equals(order.getStore().getId())) {
            throw new ApiException(ErrorStatus.ORDER_STORE_MISMATCH);
        }

        // 사용자 존재 여부 검증
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(()-> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Store store = order.getStore();

        Review review = new Review(
                order,
                user,
                store,
                request.getRating(),
                request.getContent(),
                request.getVisible()
        );

        reviewRepository.save(review);
    }

    /**
     * 리뷰 조회
     * */
    @Override
    public Page<ReviewCheckResponseDto> checkReview(Long storeId, AuthUser authUser, List<Integer> rating, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Review> reviewPage;

        // 별점이 null이거나 빈 리스트이면 전체 리뷰 조회
        if (rating == null || rating.isEmpty()) {
            reviewPage = reviewRepository.findAllByStoreId(storeId, pageable);
            return convertToResponseDtoPage(reviewPage, pageable);
        }

        // 별점 유효성 검사
        for (Integer r : rating) {
            if (r == null || r < 1 || r > 5) {
                throw new ApiException(ErrorStatus.REVIEW_INVALID_RATING);
            }
        }

        // 별점 조건으로 리뷰 조회
        reviewPage = reviewRepository.findReviewByRating(storeId, rating, pageable);
        return convertToResponseDtoPage(reviewPage, pageable);
    }

    /**
     * 리뷰 수정
     * */
    @Override
    @Transactional
    public void updateReview(Long storeId, Long reviewId, AuthUser authUser, ReviewCreateRequestDto requestDto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));
        // 리뷰가 작성자가 맞는지 검증
        if (!review.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus.REVIEW_ACCESS_DENIED);
        }
        // 가게 일치하는지 검증
        if (!review.getStore().getId().equals(storeId)) {
            throw new ApiException(ErrorStatus.REVIEW_STORE_MISMATCH);
        }

        review.update(requestDto.getRating(),requestDto.getContent(),requestDto.getVisible());
    }

    /**
     * 리뷰 삭제
     * */
    @Override
    @Transactional
    public void deleteReview(Long storeId, Long reviewId, AuthUser authUser) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));
        // 리뷰 작성자가 로그인한 유저인지 확인
        if (!review.getUser().getId().equals(authUser.getId())){
            throw new ApiException(ErrorStatus.REVIEW_ACCESS_DENIED);
        }

        // 가게 일치 확인
        if (!review.getStore().getId().equals(storeId)){
            throw new ApiException(ErrorStatus.REVIEW_STORE_MISMATCH);
        }

        // 리뷰 댓글이 존재하는지 확인
        if (review.getReply() != null) {
            review.getReply().softDelete();
        }

        // 삭제된 리뷰 일 때
        if(review.getDeleted()){
            throw new ApiException(ErrorStatus.REVIEW_ALREADY_DELETED);
        }

        review.softDelete();
    }


    // 페이징 함수 처리
    // 페이징 처리 로직을 분리하여 중복 제거
    private Page<ReviewCheckResponseDto> convertToResponseDtoPage(Page<Review> reviewPage, Pageable pageable) {
        List<ReviewCheckResponseDto> response = new ArrayList<>();
        for (Review review : reviewPage.getContent()) {
            Reply reply = review.getReply();

            ReplyDto replyDto;
            if (reply != null) {
                replyDto = new ReplyDto(reply.getId(), reply.getContent(), reply.getUpdatedAt());
            } else {
                replyDto = null;
            }

            response.add(new ReviewCheckResponseDto(
                    review.getId(),
                    review.getRating(),
                    review.getContent(),
                    review.getVisible(),
                    review.getUpdatedAt(),
                    replyDto
            ));
        }
        return new PageImpl<>(response, pageable, reviewPage.getTotalElements());
    }

//        hardDelete
//    delete : 한행=로우 하나만 지우는거
//    drop : 테이블 자체를 지우는거
//    truncate : 한행이아니라 모든 행을 지우는 거 -> 테이블은 남아있고, 데이터만 지우는 거
//
//            softDelete: 컬럼에 boolean isDeleted 추가해서 0이면 있는거고, 1이면 삭제된거고
//             -> delete 할때 repository.delete 이걸 하는게아니라, isdelete 값을 0 -> 1로 변경 그러면 1이면 삭제인거지
//
//            Review review = reviewRepostiory.findbyid(id);
//            review.softDelete()
// 리뷰 조회 순서
//    테스트 코드
//            통합테스트 (sprigboot) , 단위테스트 (mock)
//    통합테스트는 : 진짜 db 정보 불러와서 해야하는데 -> test db 만들어서
//    단위테스트는 : 가짜객체 있다치고, 얘가 있다치고 requet -> request mock 객체에 임의의 값을 넣어준다.

}
