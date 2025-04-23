package com.example.toanyone.domain.store.repository;

import com.example.toanyone.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    int countByUserId(Long ownerId);

    boolean existByName(String name);

}
