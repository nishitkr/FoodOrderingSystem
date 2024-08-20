package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.MenuItemEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM menu WHERE food_item_id = ?1")
    List<MenuItemEntity> findAllByFoodItemId(String foodItemId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT mi FROM MenuItemEntity mi WHERE mi.id = :id")
    Optional<MenuItemEntity> findByIdWithPessimisticReadLock(@Param("id") String id);
}
