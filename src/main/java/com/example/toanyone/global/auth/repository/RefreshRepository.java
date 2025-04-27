package com.example.toanyone.global.auth.repository;

import com.example.toanyone.global.auth.entity.Refresh;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    String findRefreshTokenByUserId(Long userId);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
