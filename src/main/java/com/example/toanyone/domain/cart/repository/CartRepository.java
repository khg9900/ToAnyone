package com.example.toanyone.domain.cart.repository;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.user.entity.User;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(Long id);

    Optional<Cart> findByUser(User user);
    default Cart findByUserOrElseThrow(User user) {
        return findByUser(user).orElseThrow(()-> new ApiException(ErrorStatus.USER_NOT_FOUND));
    }
}
