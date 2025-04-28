package com.example.toanyone.domain.store.repository;

import com.example.toanyone.domain.store.entity.Store;
import com.example.toanyone.global.common.code.ErrorStatus;
import com.example.toanyone.global.common.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    int countByUserIdAndDeletedFalse(Long ownerId);

    boolean existsByName(String name);
  
    Optional<Store> findById(Long storeId);

    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(()-> new ApiException(ErrorStatus.STORE_NOT_FOUND));
    }

    List<Store> findByUserIdAndDeletedFalse(Long ownerId);

    List<Store> findByNameContainingAndDeletedFalse(String keyword);

    @Query("SELECT s.user.id FROM Store s WHERE s.id = :storeId")
    Optional<Long> findOwnerIdByStoreId(@Param("storeId") Long storeId);

    default Long findOwnerIdByStoreIdOrElseThrow(Long storeId) {
        return findOwnerIdByStoreId(storeId).orElseThrow(
                ()-> new ApiException(ErrorStatus.STORE_NOT_FOUND));
    }

    @Query("SELECT s.deleted FROM Store s WHERE s.id = :id")
    Boolean getDeletedById(@Param("id") Long id);

    boolean existsByUserIdAndDeletedFalse(Long ownerId);


}
