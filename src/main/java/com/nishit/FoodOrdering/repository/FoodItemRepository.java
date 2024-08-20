package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.FoodItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItemEntity, String> {

    @Query("Select fi FROM FoodItemEntity fi WHERE fi.status = 'Available' AND fi.numberOfServingRestaurants > 0")
    List<FoodItemEntity> findAllAvailableItemsWithAtLeastOneServingRestaurant();
}
