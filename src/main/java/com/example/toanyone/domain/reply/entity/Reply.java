package com.example.toanyone.domain.reply.entity;

import com.example.toanyone.domain.review.entity.Review;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "review_reply")
@NoArgsConstructor
public class Reply extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리뷰와 1:1 관계, 하나의 리뷰에 댓글 하나만 가능
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", unique = true)
    private Review review;

    // 댓글 작성자는 사장님
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Reply(Review review, User owner, String content) {
        this.review = review;
        this.owner = owner;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
