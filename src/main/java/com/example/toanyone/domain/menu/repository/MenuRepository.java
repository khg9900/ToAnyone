package com.example.toanyone.domain.menu.repository;

import com.example.toanyone.domain.menu.entity.Menu;
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
public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findById(Long id);
    default Menu findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
                ()-> new ApiException(ErrorStatus.MENU_NOT_FOUND));
    }

    boolean existsByStoreAndName(Store store, String name);

    @Query("SELECT m.store.id FROM Menu m WHERE m.id = :id")
    Long getStoreIdById(Long id);

    @Query("SELECT m.deleted FROM Menu m WHERE m.id = :id")
    Boolean getDeletedById(@Param("id") Long id);

    boolean existsByIdAndStoreId(Long Id, Long storeId);

}
