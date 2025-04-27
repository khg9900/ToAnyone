package com.example.toanyone.domain.cart.repository;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(Long id);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);
    default Cart findByUserIdOrElseThrow(Long userId) {
        return findByUserId(userId).orElseThrow(()-> new ApiException(ErrorStatus.CART_NOT_FOUND));
    }
}
