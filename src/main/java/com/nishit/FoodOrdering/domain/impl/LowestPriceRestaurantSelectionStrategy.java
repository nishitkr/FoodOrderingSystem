package com.nishit.FoodOrdering.domain.impl;

import com.nishit.FoodOrdering.domain.RestaurantSelectionStrategy;
import com.nishit.FoodOrdering.entity.MenuItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component("lowestPrice")
@Slf4j
public class LowestPriceRestaurantSelectionStrategy extends RestaurantSelectionStrategy {

    @Override
    protected List<MenuItemEntity> getRestaurantMenuItemsSortedByStrategy(String foodItemId) {
        //TODO: Index for food_item_id in menu table
        List<MenuItemEntity> servingRestaurantMenuList = new ArrayList<>();
        try {
            servingRestaurantMenuList = menuItemRepository.findAllByFoodItemId(foodItemId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        servingRestaurantMenuList.sort(Comparator.comparing(MenuItemEntity::getPrice));
        return servingRestaurantMenuList;
    }
}
