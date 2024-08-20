package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.AddFoodItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddFoodItemRepository extends JpaRepository<AddFoodItemEntity, String> {
    public List<AddFoodItemEntity> findByRestaurantId(String restaurantId);
}
