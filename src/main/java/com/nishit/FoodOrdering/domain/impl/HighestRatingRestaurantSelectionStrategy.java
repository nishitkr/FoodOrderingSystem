package com.nishit.FoodOrdering.domain.impl;

import com.nishit.FoodOrdering.domain.RestaurantSelectionStrategy;
import com.nishit.FoodOrdering.entity.MenuItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("highestRating")
@Slf4j
public class HighestRatingRestaurantSelectionStrategy extends RestaurantSelectionStrategy {

    @Override
    protected List<MenuItemEntity> getRestaurantMenuItemsSortedByStrategy(String foodItemId) {
        List<MenuItemEntity> servingRestaurantMenuList;
        servingRestaurantMenuList = menuItemRepository.findAllByFoodItemId(foodItemId);
        servingRestaurantMenuList.sort(Comparator.comparing(MenuItemEntity::getRating).reversed());
        return servingRestaurantMenuList;
    }
}
