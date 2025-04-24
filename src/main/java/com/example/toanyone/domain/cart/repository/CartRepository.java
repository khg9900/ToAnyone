package com.example.toanyone.domain.cart.repository;

import com.example.toanyone.domain.cart.entity.Cart;
import com.example.toanyone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(Long id);

    Cart findByUser(User user);
}
