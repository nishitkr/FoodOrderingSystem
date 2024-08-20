package com.nishit.FoodOrdering.service;

import com.nishit.FoodOrdering.dto.request.OrderItemRatingRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRatingRequestDTO;
import com.nishit.FoodOrdering.entity.MenuItemEntity;
import com.nishit.FoodOrdering.entity.MenuItemRatingEntity;
import com.nishit.FoodOrdering.entity.OrderItemEntity;
import com.nishit.FoodOrdering.repository.MenuItemRatingRepository;
import com.nishit.FoodOrdering.repository.MenuItemRepository;
import com.nishit.FoodOrdering.repository.OrderItemRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrderItemRatingService {

    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private MenuItemRatingRepository menuItemRatingRepository;
    @Autowired private MenuItemRepository menuItemRepository;

    @Autowired private Validator<OrderRatingRequestDTO> orderRatingRequestValidatorDTO;

    @Transactional
    public void handleOrderItemRating(OrderRatingRequestDTO orderRatingRequestDTO) {
        //update rating in order item table
        //if menu item + rating exists
        // update rating count for corresponding menu item + rating in menu item rating table
        //else add rating for corresponding menu item in meu item rating table
        //Fetch all ratings for corresponding menu item
        //Calculate overall rating
        //Update rating in menu table
        orderRatingRequestValidatorDTO.validate(orderRatingRequestDTO);
        for (OrderItemRatingRequestDTO orderItemRatingRequestDTO : orderRatingRequestDTO.getOrderItemsRating()) {
            OrderItemEntity orderItemEntityExisting = orderItemRepository.findById(orderItemRatingRequestDTO.getOrderItemId()).orElse(null);
            if (Objects.nonNull(orderItemEntityExisting)) {
                if (Objects.isNull(orderItemEntityExisting.getRating()) || orderItemEntityExisting.getRating() <= 0) {
                    orderItemEntityExisting.updateRating(orderItemRatingRequestDTO.getRating());
                    orderItemRepository.save(orderItemEntityExisting);
                    saveRatingForMenuItemOfOrderItem(orderItemEntityExisting, orderItemRatingRequestDTO.getRating());
                } else {
                    log.warn("OrderItemId : {} was already rated, skipping rating update for the same", orderItemRatingRequestDTO.getOrderItemId());
                }
            } else {
                log.error("Data missing for OrderItemId : {}, skipping and not saving the rating", orderItemRatingRequestDTO.getOrderItemId());
            }
        }
    }

    public void saveRatingForMenuItemOfOrderItem(OrderItemEntity orderItemEntity, int rating) {
        addOrUpdateRating(orderItemEntity.getMenuItemId(), rating);
        List<MenuItemRatingEntity> menuItemRatings = menuItemRatingRepository.findAllByMenuItemIdWithPessimisticReadLock(orderItemEntity.getMenuItemId());
        MenuItemEntity menuItemEntity = menuItemRepository.findByIdWithPessimisticReadLock(orderItemEntity.getMenuItemId()).orElse(null);
        if (Objects.nonNull(menuItemEntity)) {
            double newMenuItemRating = calculateOverallRating(menuItemRatings);
            menuItemEntity.updateRating(newMenuItemRating);
            menuItemRepository.save(menuItemEntity);
        } else {
            log.error("Data missing from menu table for menuItemId: {}, skipped updating rating", orderItemEntity.getMenuItemId());
        }
    }

    public void addOrUpdateRating(String menuItemId, int rating) {
        MenuItemRatingEntity menuItemRatingEntityExisting = menuItemRatingRepository
                .findByIdWithPessimisticReadLock(menuItemId, rating)
                .orElse(null);
        if (Objects.nonNull(menuItemRatingEntityExisting)) {
            menuItemRatingEntityExisting.incrementRatingCount();
            log.info("Incremented rating count for menuItemId: {} and rating: {} to count: {}",
                    menuItemId, rating, menuItemRatingEntityExisting.getRatingCount());
            menuItemRatingRepository.save(menuItemRatingEntityExisting);
        } else {
            log.info("Initializing rating for menuItemId: {} and rating: {}", menuItemId, rating);
            MenuItemRatingEntity menuItemRatingEntityNew = initializeMenuItemRating(menuItemId, rating);
            menuItemRatingRepository.save(menuItemRatingEntityNew);
        }
    }

    public MenuItemRatingEntity initializeMenuItemRating(String menuItemId, int rating) {
        return MenuItemRatingEntity.builder()
                .menuItemId(menuItemId)
                .rating(rating)
                .ratingCount(1)
                .build();
    }

    public double calculateOverallRating(List<MenuItemRatingEntity> menuItemRatings) {
        double ratingsSum = 0.0;
        double totalRatingCount = 0.0;
        for (MenuItemRatingEntity menuItemRatingEntity : menuItemRatings) {
            double currentRatingCount = Double.valueOf(menuItemRatingEntity.getRatingCount());
            ratingsSum += Double.valueOf(menuItemRatingEntity.getRating()) * currentRatingCount;
            totalRatingCount += currentRatingCount;
        }
        double overallRating = ratingsSum/totalRatingCount;
        return new BigDecimal(overallRating).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
