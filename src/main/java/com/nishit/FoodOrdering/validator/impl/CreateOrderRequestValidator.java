package com.nishit.FoodOrdering.validator.impl;

import com.nishit.FoodOrdering.dto.request.OrderItemRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRequestDTO;
import com.nishit.FoodOrdering.entity.FoodItemEntity;
import com.nishit.FoodOrdering.entity.UserEntity;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.repository.FoodItemRepository;
import com.nishit.FoodOrdering.repository.UserRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class CreateOrderRequestValidator implements Validator<OrderRequestDTO> {

    @Autowired private UserRepository userRepository;
    @Autowired private FoodItemRepository foodItemRepository;

    @Override
    public void validate(OrderRequestDTO orderRequestDTO) {
        UserEntity userEntity = userRepository.findById(orderRequestDTO.getUserId()).orElse(null);
        if (Objects.isNull(userEntity)) {
            log.error("CreateOrder :: User id not found in system with id:{}", orderRequestDTO.getUserId());
            throw new DataNotFoundException("Invalid User ID");
        } else {
            for (OrderItemRequestDTO orderItemRequestDTO : orderRequestDTO.getOrderItems()) {
                FoodItemEntity foodItemEntity = foodItemRepository.findById(orderItemRequestDTO.getFoodItemId()).orElse(null);
                if (Objects.isNull(foodItemEntity)) {
                    log.error("CreateOrder :: Food Item ID not found in system with id:{}", orderItemRequestDTO.getFoodItemId());
                    throw new DataNotFoundException("Invalid food item id : " + orderItemRequestDTO.getFoodItemId());
                }
            }
        }
    }
}
