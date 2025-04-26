package com.example.toanyone.global.auth.repository;

import com.example.toanyone.global.auth.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    @Query("SELECT r.refresh FROM Refresh r WHERE r.userId = :userId")
    String getRefreshToken(@Param("userId") Long userId);

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
