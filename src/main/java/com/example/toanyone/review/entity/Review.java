package com.example.toanyone.review.entity;

import jakarta.persistence.*;

// 깃 병합 연습
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
