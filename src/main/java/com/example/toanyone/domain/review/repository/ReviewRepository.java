package com.example.toanyone.domain.review.repository;

import com.example.toanyone.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    // 리뷰 별점으로 조회하는 쿼리문
    @Query("select r from Review r where r.store.id = :storeId and r.rating in :rating and r.deleted = false order by r.createdAt desc")
    Page<Review> findReviewByRating(@Param("storeId") Long storeId, @Param("rating") List<Integer> rating, Pageable pageable);


    // 리뷰 조회 최신순으로 해주는
    @Query("select r from Review r where r.store.id = :storeId and r.deleted = false order by r.createdAt desc")
    Page<Review> findAllByStoreId(@Param("storeId") Long storeId, Pageable pageable);
}
