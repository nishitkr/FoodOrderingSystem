package com.nishit.FoodOrdering.service;

import com.nishit.FoodOrdering.dto.request.CreateMenuDTO;
import com.nishit.FoodOrdering.dto.request.CreateMenuItemDTO;
import com.nishit.FoodOrdering.entity.FoodItemEntity;
import com.nishit.FoodOrdering.entity.MenuItemEntity;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.repository.FoodItemRepository;
import com.nishit.FoodOrdering.repository.MenuItemRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    public List<MenuItemEntity> prepareMenuItemList(CreateMenuDTO createMenuDTO) {
        List<MenuItemEntity> menuItems = new ArrayList<>();
        String restaurantId = createMenuDTO.getRestaurantId();
        for (CreateMenuItemDTO createMenuItemDTO : createMenuDTO.getMenuItemList()) {
            Optional<FoodItemEntity> foodItemEntityOptional = foodItemRepository.findById(createMenuItemDTO.getFoodItemId());
            if (foodItemEntityOptional.isPresent()) {
                menuItems.add(MenuItemEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .price(createMenuItemDTO.getPrice())
                        .createdOn(Timestamp.from(Instant.now()))
                        .modifiedOn(Timestamp.from(Instant.now()))
                        .restaurantId(restaurantId)
                        .rating(0.0)
                        .foodItem(foodItemEntityOptional.get())
                        .build());
            } else {
                log.error("Invalid food item id provided in menu, food_item_id : {}", createMenuItemDTO.getFoodItemId());
                throw new DataNotFoundException("Menu rejected as no food item exists with ID : " + createMenuItemDTO.getFoodItemId());
            }
        }

        return menuItems;
    }

    public void saveMenuItems(List<MenuItemEntity> menuItems) {
        menuItemRepository.saveAll(menuItems);
        for (MenuItemEntity menuItemEntity : menuItems) {
            incrementNumberOfServingRestaurant(menuItemEntity.getFoodItem().getId());
        }
    }

    public List<MenuItemEntity> getMenuItemsById(List<String> menuItemIds) {
        return menuItemRepository.findAllById(menuItemIds);
    }

    @Synchronized
    private void incrementNumberOfServingRestaurant(String foodItemId) {
        Optional<FoodItemEntity> foodItemEntityOptional = foodItemRepository.findById(foodItemId);
        if (foodItemEntityOptional.isPresent()) {
            FoodItemEntity foodItemEntity = foodItemEntityOptional.get();
            foodItemEntity.incrementNumberOfServingRestaurantByOne();
            foodItemRepository.save(foodItemEntity);
        } else {
            //TODO: throw error
        }
    }
}
