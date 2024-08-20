package com.nishit.FoodOrdering.validator.impl;

import com.nishit.FoodOrdering.dto.request.OrderItemRatingRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRatingRequestDTO;
import com.nishit.FoodOrdering.entity.OrderEntity;
import com.nishit.FoodOrdering.entity.OrderItemEntity;
import com.nishit.FoodOrdering.enums.OrderStatus;
import com.nishit.FoodOrdering.exception.DataNotFoundException;
import com.nishit.FoodOrdering.exception.OperationNotAllowedException;
import com.nishit.FoodOrdering.repository.OrderItemRepository;
import com.nishit.FoodOrdering.repository.OrderRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class OrderRatingRequestValidatorDTO implements Validator<OrderRatingRequestDTO> {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;

    @Override
    public void validate(OrderRatingRequestDTO orderRatingRequestDTO) {
        OrderEntity order = orderRepository.findById(orderRatingRequestDTO.getOrderId()).orElse(null);
        if (Objects.isNull(order)) {
            log.error("Order not found in the system with id:{}", orderRatingRequestDTO.getOrderId());
            throw new DataNotFoundException("Order not found in the system");
        } else {
            List<String> orderItemIds = orderRatingRequestDTO.getOrderItemsRating().stream().map(OrderItemRatingRequestDTO::getOrderItemId).toList();
            List<OrderItemEntity> orderItemEntityList = orderItemRepository.findAllById(orderItemIds);
            for (OrderItemEntity orderItemEntity : orderItemEntityList) {
                if (!orderRatingRequestDTO.getOrderId().equals(orderItemEntity.getOrderId())) {
                    log.error("Order Item with id : {} doesn't belong to orderId : {}", orderItemEntity.getId(), orderRatingRequestDTO.getOrderId());
                    throw new OperationNotAllowedException("Order Item Id : " + orderItemEntity.getId() + " doesn't belong to provided order Id");
                }
                if (!OrderStatus.DISPATCHED.value().equals(orderItemEntity.getStatus())) {
                    log.error("Order Item Id : {} is not yet dispatched, rating can't be accepted", orderItemEntity.getId());
                    throw new OperationNotAllowedException("Order Item Id : " + orderItemEntity.getId() + " is not yet dispatched. Rating can't be accepted.");
                }
            }
        }
    }
}
