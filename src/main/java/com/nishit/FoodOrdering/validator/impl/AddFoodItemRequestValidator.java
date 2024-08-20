package com.nishit.FoodOrdering.validator.impl;

import com.nishit.FoodOrdering.dto.request.AddFoodItemRequestDTO;
import com.nishit.FoodOrdering.entity.RestaurantEntity;
import com.nishit.FoodOrdering.enums.ApprovalStatus;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.repository.RestaurantRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class AddFoodItemRequestValidator implements Validator<AddFoodItemRequestDTO> {

    @Autowired private RestaurantRepository restaurantRepository;

    @Override
    public void validate(AddFoodItemRequestDTO addFoodItemRequestDTO) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(addFoodItemRequestDTO.getRestaurantId()).orElse(null);
        if (Objects.nonNull(restaurantEntity)) {
            if (!ApprovalStatus.APPROVED.value().equals(restaurantEntity.getStatus())) {
                log.error("AddFoodItemRequestValidator :: Request received from unapproved restaurant with id:{}", restaurantEntity.getId());
                throw new OperationNotAllowedException("Add item food request can only be created by approved restaurant.");
            }
        } else {
            log.error("AddFoodItemRequestValidator :: Restaurant not found in database with id:{}", addFoodItemRequestDTO.getRestaurantId());
            throw new DataNotFoundException("Restaurant not found in system");
        }
    }
}
