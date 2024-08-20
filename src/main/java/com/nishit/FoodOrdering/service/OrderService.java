package com.nishit.FoodOrdering.service;

import com.nishit.FoodOrdering.domain.RestaurantSelectionStrategyFactory;
import com.nishit.FoodOrdering.dto.OrderItemDispatchDTO;
import com.nishit.FoodOrdering.dto.request.OrderItemDispatchRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRequestDTO;
import com.nishit.FoodOrdering.dto.OrderDTO;
import com.nishit.FoodOrdering.dto.OrderItemDTO;
import com.nishit.FoodOrdering.entity.FoodItemEntity;
import com.nishit.FoodOrdering.entity.OrderEntity;
import com.nishit.FoodOrdering.entity.OrderItemEntity;
import com.nishit.FoodOrdering.entity.RestaurantEntity;
import com.nishit.FoodOrdering.enums.OrderStatus;
import com.nishit.FoodOrdering.repository.FoodItemRepository;
import com.nishit.FoodOrdering.repository.OrderItemRepository;
import com.nishit.FoodOrdering.repository.OrderRepository;
import com.nishit.FoodOrdering.repository.RestaurantRepository;
import com.nishit.FoodOrdering.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private RestaurantRepository restaurantRepository;

    @Autowired private RestaurantSelectionStrategyFactory restaurantSelectionStrategyFactory;

    @Autowired private Validator<OrderRequestDTO> createOrderRequestValidator;
    @Autowired private Validator<OrderItemDispatchRequestDTO> orderItemDispatchRequestValidator;

    public OrderDTO handleOrderCreation(OrderRequestDTO orderRequestDTO) {
        createOrderRequestValidator.validate(orderRequestDTO);
        OrderEntity order = restaurantSelectionStrategyFactory.getRestaurantSelector().assignOrderToRestaurant(orderRequestDTO);
        return convertOrderEntityToDTO(order);
    }

    @Transactional
    public List<OrderItemDispatchDTO> handleOrderItemDispatch(OrderItemDispatchRequestDTO orderItemDispatchRequestDTO) {
        //Update order item status
        //release restaurant processing capacity
        //Check if all items of order are dispatched - if yes, mark Order as dispatched
        orderItemDispatchRequestValidator.validate(orderItemDispatchRequestDTO);
        List<OrderItemDispatchDTO> orderItemDispatchDTOList = new ArrayList<>();
        for (String orderItemId : orderItemDispatchRequestDTO.getOrderItemIds()) {
            orderItemDispatchDTOList.add(updateStatusAndReleaseCapacity(orderItemId));
        }
        return  orderItemDispatchDTOList;
    }

    public OrderItemDispatchDTO updateStatusAndReleaseCapacity(String orderItemId) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderItemId).orElse(null);
        if (Objects.nonNull(orderItemEntity)) {
            if (OrderStatus.DISPATCHED.value().equals(orderItemEntity.getStatus())) {
                return OrderItemDispatchDTO.builder()
                        .orderItemId(orderItemId)
                        .errorDescription("Order item already dispatched")
                        .build();
            }
            orderItemEntity.dispatchOrderItem();
            orderItemRepository.save(orderItemEntity);

            RestaurantEntity restaurantEntity = restaurantRepository
                    .findByIdWithPessimisticReadLock(orderItemEntity.getRestaurantId())
                    .orElse(null);
            if (Objects.nonNull(restaurantEntity)) {
                restaurantEntity.incrementProcessingCapacity(orderItemEntity.getQuantity());
                restaurantRepository.save(restaurantEntity);

                checkAndUpdateOrderStatus(orderItemEntity.getOrderId());

                return OrderItemDispatchDTO.builder()
                        .orderItemId(orderItemId)
                        .restaurantId(restaurantEntity.getId())
                        .restaurantName(restaurantEntity.getName())
                        .processingCapacity(restaurantEntity.getProcessingCapacity())
                        .errorDescription(null)
                        .build();
            } else {
                log.error("Restaurant with id:{} missing from database. Updated order item status but restaurant capacity not incremented",
                        orderItemEntity.getRestaurantId());
                return OrderItemDispatchDTO.builder()
                        .orderItemId(orderItemId)
                        .errorDescription("Restaurant missing from database")
                        .build();
            }

        } else {
            log.warn("Order Item with OrderItemId: {} not found in database, skipping it", orderItemId);
            return OrderItemDispatchDTO.builder()
                    .orderItemId(orderItemId)
                    .errorDescription("Invalid Order Item Id")
                    .build();
        }
    }

    public void checkAndUpdateOrderStatus(String orderId) {
        List<OrderItemEntity> orderItemEntityList = orderItemRepository.findAllByOrderId(orderId);
        long countOfPendingOrderItems = orderItemEntityList.stream()
                .filter(orderItemEntity -> !OrderStatus.DISPATCHED.value().equals(orderItemEntity.getStatus()))
                .count();
        if (countOfPendingOrderItems == 0L) {
            OrderEntity order = orderRepository.findById(orderId).orElse(null);
            if (Objects.nonNull(order)) {
                order.updateOrderAsDispatched();
                orderRepository.save(order);
            } else {
                log.error("Order data missing for OrderId:{}", orderId);
            }
        }
    }

    public OrderDTO convertOrderEntityToDTO(OrderEntity order) {
        List<OrderItemDTO> orderItems = convertOrderItemEntitiesToDTO(order.getOrderItems());
        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .createdOn(order.getCreatedAt())
                .orderItems(orderItems)
                .build();
    }

    public List<OrderItemDTO> convertOrderItemEntitiesToDTO(List<OrderItemEntity> orderItemEntityList) {
        return orderItemEntityList.stream().map(this::convertOrderItemEntityToDTO).collect(Collectors.toList());
    }

    public OrderItemDTO convertOrderItemEntityToDTO(OrderItemEntity orderItemEntity) {
        FoodItemEntity foodItemEntity = foodItemRepository.findById(orderItemEntity.getFoodItemId()).orElse(null);
        RestaurantEntity restaurantEntity = restaurantRepository.findById(orderItemEntity.getRestaurantId()).orElse(null);
        return OrderItemDTO.builder()
                .id(orderItemEntity.getId())
                .quantity(orderItemEntity.getQuantity())
                .unitPrice(orderItemEntity.getUnitPrice())
                .itemPrice(orderItemEntity.getItemPrice())
                .foodItemId(orderItemEntity.getFoodItemId())
                .foodItemName(Objects.nonNull(foodItemEntity) ? foodItemEntity.getName() : "")
                .restaurantId(orderItemEntity.getRestaurantId())
                .restaurantName(Objects.nonNull(restaurantEntity) ? restaurantEntity.getName() : "")
                .status(orderItemEntity.getStatus())
                .rating(Objects.nonNull(orderItemEntity.getRating()) ? orderItemEntity.getRating() : 0)
                .build();
    }
}
