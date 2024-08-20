package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.MenuItemRatingEntity;
import com.nishit.FoodOrdering.entity.MenuItemRatingId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRatingRepository extends JpaRepository<MenuItemRatingEntity, MenuItemRatingId> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r FROM MenuItemRatingEntity r WHERE r.menuItemId = :menuItemId")
    List<MenuItemRatingEntity> findAllByMenuItemIdWithPessimisticReadLock(@Param("menuItemId") String menuItemId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r FROM MenuItemRatingEntity r WHERE r.menuItemId = :menuItemId AND r.rating = :rating")
    Optional<MenuItemRatingEntity> findByIdWithPessimisticReadLock(@Param("menuItemId") String menuItemId, @Param("rating") Integer rating);
}
