//package com.example.toanyone.domain.reply.service;
//
//import com.example.toanyone.domain.reply.dto.ReplyRequestDto;
//import com.example.toanyone.domain.reply.dto.ReplyResponseDto;
//import com.example.toanyone.domain.reply.entity.Reply;
//import com.example.toanyone.domain.reply.repository.ReplyRepository;
//import com.example.toanyone.domain.review.entity.Review;
//import com.example.toanyone.domain.review.repository.ReviewRepository;
//import com.example.toanyone.domain.user.entity.User;
//import com.example.toanyone.domain.user.enums.UserRole;
//import com.example.toanyone.domain.user.repository.UserRepository;
//import com.example.toanyone.global.auth.dto.AuthUser;
//import com.example.toanyone.global.common.error.ApiException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(SpringExtension.class)
//public class ReplyServiceTest {
//    @Mock
//    private ReplyRepository replyRepository;
//    @Mock
//    private ReviewRepository reviewRepository;
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private ReplyServiceImpl replyService;
//
//    @Test
//    void 댓글_생성_성공() {
//        Long ownerId = 1L;
//        Long storeId = 1L;
//        Long reviewId = 1L;
//
//        AuthUser authUser = new AuthUser(ownerId, "owner@test.com", "OWNER");
//
//        User owner = new User("owner@test.com", "password123", "사장님", UserRole.OWNER, "nickname", "010-1234-5678", "서울시 강남구", "MALE", "1990-01-01");
//        ReflectionTestUtils.setField(owner, "id", ownerId);
//
//        Review review = new Review();
//        ReflectionTestUtils.setField(review, "store", new com.example.toanyone.domain.store.entity.Store());
//        ReflectionTestUtils.setField(review.getStore(), "id", storeId);
//
//        ReplyRequestDto requestDto = new ReplyRequestDto("감사합니다!");
//
//        // Given
//        given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
//        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
//
//        // When
//        ReplyResponseDto result = replyService.createReply(storeId, reviewId, authUser, requestDto);
//
//        // Then
//        assertEquals("댓글이 등록되었습니다.", result.getMessage());
//    }
//
//    @Test
//    void 댓글_생성_실패_이미_댓글존재() {
//        Long ownerId = 1L;
//        Long storeId = 1L;
//        Long reviewId = 1L;
//
//        AuthUser authUser = new AuthUser(ownerId, "owner@test.com", "OWNER");
//
//
//        User owner = new User("owner@test.com", "password123", "사장님", UserRole.OWNER, "nickname", "010-1234-5678", "서울시 강남구", "MALE", "1990-01-01");
//        ReflectionTestUtils.setField(owner, "id", ownerId);
//
//        Review review = new Review();
//        ReflectionTestUtils.setField(review, "store", new com.example.toanyone.domain.store.entity.Store());
//        ReflectionTestUtils.setField(review.getStore(), "id", storeId);
//
//
//        Reply existingReply = new Reply();
//        ReflectionTestUtils.setField(existingReply, "id", 100L);
//        ReflectionTestUtils.setField(review, "reply", existingReply);
//
//        ReplyRequestDto requestDto = new ReplyRequestDto("감사합니다!");
//
//        // Given
//        given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
//        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
//
//        // When & Then
//        ApiException exception = assertThrows(ApiException.class,
//                () -> replyService.createReply(storeId, reviewId, authUser, requestDto));
//
//        assertEquals("이미 댓글이 존재합니다.", exception.getMessage());
//    }
//
//    @Test
//    void 댓글_수정_성공() {
//        Long ownerId = 1L;
//        Long storeId = 1L;
//        Long reviewId = 1L;
//
//        AuthUser authUser = new AuthUser(ownerId, "owner@test.com", "OWNER");
//
//        User owner = new User("owner@test.com", "password123", "사장님", UserRole.OWNER, "nickname", "010-1234-5678", "서울시 강남구", "MALE", "1990-01-01");
//        ReflectionTestUtils.setField(owner, "id", ownerId);
//
//        Review review = new Review();
//        ReflectionTestUtils.setField(review, "store", new com.example.toanyone.domain.store.entity.Store());
//        ReflectionTestUtils.setField(review.getStore(), "id", storeId);
//
//        Reply reply = new Reply();
//        ReflectionTestUtils.setField(reply, "id", 1L);
//        ReflectionTestUtils.setField(reply, "owner", owner);
//        ReflectionTestUtils.setField(review, "reply", reply);
//
//        ReplyRequestDto updateRequest = new ReplyRequestDto("수정된 댓글입니다!");
//
//        // Given
//        given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
//        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
//
//        // When
//        ReplyResponseDto result = replyService.updateReply(storeId, reviewId, authUser, updateRequest);
//
//        // Then
//        assertEquals("댓글이 수정되었습니다.", result.getMessage());
//    }
//
//    @Test
//    void 댓글_삭제_성공() {
//        Long ownerId = 1L;
//        Long storeId = 1L;
//        Long reviewId = 1L;
//
//        AuthUser authUser = new AuthUser(ownerId, "owner@test.com", "OWNER");
//
//        User owner = new User("owner@test.com", "password123", "사장님", UserRole.OWNER, "nickname", "010-1234-5678", "서울시 강남구", "MALE", "1990-01-01");
//        ReflectionTestUtils.setField(owner, "id", ownerId);
//
//        Review review = new Review();
//        ReflectionTestUtils.setField(review, "store", new com.example.toanyone.domain.store.entity.Store());
//        ReflectionTestUtils.setField(review.getStore(), "id", storeId);
//
//        Reply reply = new Reply();
//        ReflectionTestUtils.setField(reply, "id", 1L);
//        ReflectionTestUtils.setField(reply, "owner", owner);
//        ReflectionTestUtils.setField(reply, "deleted", false);
//        ReflectionTestUtils.setField(review, "reply", reply);
//
//        // Given
//        given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
//        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
//
//        // When
//        ReplyResponseDto result = replyService.deleteReply(storeId, reviewId, authUser);
//
//        // Then
//        assertEquals("댓글이 정상적으로 삭제되었습니다.", result.getMessage());
//    }
//}