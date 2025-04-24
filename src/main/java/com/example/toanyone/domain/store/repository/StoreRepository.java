package com.example.toanyone.domain.store.repository;

import com.example.toanyone.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findById(Long storeId);

    int countByUserId(Long ownerId);

    boolean existsByName(String name);
  
    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(()-> new RuntimeException()); //ErrorCode 적용 후 변경
    }

}
