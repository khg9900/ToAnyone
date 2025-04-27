package com.example.toanyone.global.auth.repository;

import com.example.toanyone.global.auth.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Refresh findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);
}
