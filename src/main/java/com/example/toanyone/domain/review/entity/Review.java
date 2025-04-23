package com.example.toanyone.domain.review.entity;

import com.example.toanyone.domain.order.entity.Order;
import com.example.toanyone.domain.reply.entity.Reply;
import com.example.toanyone.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 하나의 주문은 하나의 리뷰만 작성 가능 하도록
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Validation 검증
    @Min(1)
    @Max(5)
    private int rating;

    @Column(nullable = false)
    private boolean visible = true;


    /**
     * @mappedBy = "review": 리뷰가 주인이 아니고 댓글이 외래키를 가짐
     * @cascade = ALL: 리뷰를 저장/삭제하면 댓글도 자동으로 함께 저장/삭제
     * @orphanRemoval = true: 리뷰가 삭제되면 댓글도 고아 객체로 간주되고 삭제
     * **/
    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Reply reply;
}
