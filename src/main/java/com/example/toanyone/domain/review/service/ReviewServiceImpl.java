package com.example.toanyone.domain.review.service;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.order.enums.OrderStatus;
import com.example.toanyone.domain.order.repository.OrderRepository;
import com.example.toanyone.domain.reply.dto.ReplyDto;
import com.example.toanyone.domain.reply.entity.Reply;
import com.example.toanyone.domain.reply.repository.ReplyRepository;
import com.example.toanyone.domain.review.dto.ReviewCheckResponseDto;
import com.example.toanyone.domain.review.dto.ReviewCreateRequestDto;
import com.example.toanyone.domain.review.dto.ReviewResponseDto;
import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.review.repository.ReviewRepository;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.domain.user.repository.UserRepository;
import com.example.toanyone.global.common.dto.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReviewResponseDto createReview(Long storeId, AuthUser authUser, ReviewCreateRequestDto request){

        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("not found user"));

        Order order = orderRepository.findByStoreIdAndUserIdAndStatusAndReviewIsNull(storeId, user.getId(), OrderStatus.DELIVERED)
                .orElseThrow(() -> new IllegalArgumentException("배달 완료된 주문만 리뷰를 작성할 수 있습니다."));

        Review review = new Review(
                order,
                user,
                request.getRating(),
                request.getContent(),
                request.getVisible()
        );

        reviewRepository.save(review);

        return new ReviewResponseDto("리뷰가 작성되었습니다.");
    }

    // 리뷰 조회
    @Override
    public Page<ReviewCheckResponseDto> checkReview(Long storeId, AuthUser authUser, List<Integer> rating, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size); // 페이지 사이즈 설정 
        
        //로그인한 유저인지 확인 어스유저 통해서 확인
        userRepository.findById(authUser.getId()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        
        Page<Review> review; 

        if(rating != null && rating.isEmpty()){
            for(Integer r : rating) {
                if (r < 1 || r > 5) {
                    throw new IllegalArgumentException("별점은 1점에서 5점 사이만 조회 가능합니다.");
                }
            }
            review = reviewRepository.findAllStoreIdAndRating(storeId,rating,pageable); // 별점 기준 리뷰 조회 
        } else {
            review = reviewRepository.findAllByStoreId(storeId, pageable); // 별점 상관 없이 리뷰 다 뜨는거
        }

        /**
         * response: 실제로 결과값을 저장할 dto
         * a18: ReviewCheckResponseDto 에 담아줄 Reivew 객체
         * review.getContent(): 페이징에서 리스트로
         * responseDto: a18 을 담아서 실제로 결과값을 저장할 dto 에 add
         * */
        List<ReviewCheckResponseDto> response = new ArrayList<>();
        for(Review a18 : review.getContent()) {
            Reply reply = a18.getReply();
            ReviewCheckResponseDto responseDto = new ReviewCheckResponseDto(a18.getId(),
                    a18.getRating(),
                    a18.getContent(),
                    a18.getVisible(),
                    a18.getUpdatedAt(),
                    new ReplyDto(reply.getId(), reply.getContent(), reply.getUpdatedAt()));
            response.add(responseDto);
    }
        return new PageImpl<>(response, pageable, review.getTotalElements());
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
//
//    테스트 코드
//            통합테스트 (sprigboot) , 단위테스트 (mock)
//    통합테스트는 : 진짜 db 정보 불러와서 해야하는데 -> test db 만들어서
//    단위테스트는 : 가짜객체 있다치고, 얘가 있다치고 requet -> request mock 객체에 임의의 값을 넣어준다.

}
