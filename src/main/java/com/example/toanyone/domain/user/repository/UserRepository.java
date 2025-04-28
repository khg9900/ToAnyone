package com.example.toanyone.domain.user.repository;

import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long Id) {
        return findById(Id).orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);
}
