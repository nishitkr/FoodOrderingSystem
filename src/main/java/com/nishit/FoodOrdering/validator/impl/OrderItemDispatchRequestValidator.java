package com.nishit.FoodOrdering.validator.impl;

import com.nishit.FoodOrdering.dto.request.OrderItemDispatchRequestDTO;
import com.nishit.FoodOrdering.entity.OrderItemEntity;
import com.nishit.FoodOrdering.entity.RestaurantEntity;
import com.nishit.FoodOrdering.enums.ApprovalStatus;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.repository.OrderItemRepository;
import com.nishit.FoodOrdering.repository.RestaurantRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class OrderItemDispatchRequestValidator implements Validator<OrderItemDispatchRequestDTO> {

    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private OrderItemRepository orderItemRepository;

    @Override
    public void validate(OrderItemDispatchRequestDTO orderItemDispatchRequestDTO) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(orderItemDispatchRequestDTO.getRestaurantId()).orElse(null);
        if(Objects.isNull(restaurantEntity)) {
            log.error("Restaurant not found in system with id : {}", orderItemDispatchRequestDTO.getRestaurantId());
            throw new DataNotFoundException("Restaurant not found in system");
        } else {
            if (!ApprovalStatus.APPROVED.value().equals(restaurantEntity.getStatus())) {
                log.error("Unapproved restaurant is not allowed to dispatch order items. Check how order got assigned to this restaurant.");
                throw new OperationNotAllowedException("Unapproved restaurant is not allowed to dispatch order items");
            } else {
                List<OrderItemEntity> orderItemEntityList = orderItemRepository.findAllById(orderItemDispatchRequestDTO.getOrderItemIds());
                List<String> orderItemIdsFromRequest = new ArrayList<>(orderItemDispatchRequestDTO.getOrderItemIds());
                List<String> orderItemIdsFromDatabase = orderItemEntityList.stream().map(OrderItemEntity::getId).toList();
                orderItemIdsFromRequest.removeAll(orderItemIdsFromDatabase);
                if (!orderItemIdsFromRequest.isEmpty()) {
                    log.error("Order Item IDs : {} not present in system", orderItemIdsFromRequest);
                    throw new DataNotFoundException("Order Item IDs : " + orderItemIdsFromRequest + "not present in system");
                } else {
                    for (OrderItemEntity orderItemEntity : orderItemEntityList) {
                        if (!orderItemDispatchRequestDTO.getRestaurantId().equals(orderItemEntity.getRestaurantId())) {
                            log.error("ALl order item ids do not belong to restaurant provided in request : {}", orderItemDispatchRequestDTO.getRestaurantId());
                            throw new OperationNotAllowedException("Order Item ID : " + orderItemEntity.getId() + " doesn't belong to the provided restaurant");
                        }
                    }
                }
            }
        }
    }
}
