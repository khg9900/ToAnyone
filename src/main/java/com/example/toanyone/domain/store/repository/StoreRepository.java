package com.example.toanyone.domain.store.repository;

import com.example.toanyone.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    int countByUserIdAndDeletedFalse(Long ownerId);

    boolean existsByName(String name);
  
    Optional<Store> findById(Long storeId);

    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(()-> new RuntimeException()); //ErrorCode 적용 후 변경
    }

    List<Store> findByUserIdAndDeletedFalse(Long ownerId);

    List<Store> findByNameContainingAndDeletedFalse(String keyword);

}
