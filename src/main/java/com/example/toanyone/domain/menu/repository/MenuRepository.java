package com.example.toanyone.domain.menu.repository;

import com.example.toanyone.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findById(Long id);
    default Menu findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new RuntimeException());
    }
}
