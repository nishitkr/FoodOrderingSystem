package com.nishit.FoodOrdering.domain;

import com.nishit.FoodOrdering.dto.request.OrderItemRequestDTO;
import com.nishit.FoodOrdering.dto.request.OrderRequestDTO;
import com.nishit.FoodOrdering.entity.MenuItemEntity;
import com.nishit.FoodOrdering.entity.OrderEntity;
import com.nishit.FoodOrdering.entity.OrderItemEntity;
import com.nishit.FoodOrdering.entity.RestaurantEntity;
import com.nishit.FoodOrdering.enums.ApprovalStatus;
import com.nishit.FoodOrdering.enums.OrderStatus;
import com.nishit.FoodOrdering.exception.OrderNotFulfilledException;
import com.nishit.FoodOrdering.repository.MenuItemRepository;
import com.nishit.FoodOrdering.repository.OrderItemRepository;
import com.nishit.FoodOrdering.repository.OrderRepository;
import com.nishit.FoodOrdering.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
public abstract class RestaurantSelectionStrategy {

    @Autowired protected MenuItemRepository menuItemRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;

    protected abstract List<MenuItemEntity> getRestaurantMenuItemsSortedByStrategy(String foodItemId);

    Predicate<Optional<RestaurantEntity>> restaurantValidator = (restaurantEntityOptional) -> {
        if (restaurantEntityOptional.isEmpty()) {
            log.error("Restaurant data missing");
            return false;
        }
        RestaurantEntity restaurantEntity = restaurantEntityOptional.get();
        if (!ApprovalStatus.APPROVED.value().equals(restaurantEntity.getStatus())) {
            log.error("Restaurant with id : {} is not approved. Check how menu got created for this", restaurantEntity.getId());
            return false;
        }

        if (restaurantEntity.getProcessingCapacity() <= 0) {
            log.error("Restaurant with id : {} doesn't have any processing capacity", restaurantEntity.getId());
            return false;
        }
        return true;
    };

    @Transactional
//    @Lock(LockModeType.PESSIMISTIC_READ) - Try this here
    public OrderEntity assignOrderToRestaurant(OrderRequestDTO orderRequestDTO) {
        OrderEntity order = initialiseOrderEntity(orderRequestDTO);

        List<OrderItemEntity> orderItemsList = new ArrayList<>();

        List<OrderItemRequestDTO> orderedItems = orderRequestDTO.getOrderItems();
        for (OrderItemRequestDTO currentOrderItem : orderedItems) {
            List<MenuItemEntity> servingRestaurantMenuList = getRestaurantMenuItemsSortedByStrategy(currentOrderItem.getFoodItemId());
            int remainingQuantity = currentOrderItem.getQuantity();
            for (MenuItemEntity restaurantMenuItem : servingRestaurantMenuList) {
                Optional<RestaurantEntity> restaurantEntityOptional
                        = restaurantRepository.findByIdWithPessimisticReadLock(restaurantMenuItem.getRestaurantId());
                boolean isValid = restaurantValidator.test(restaurantEntityOptional);
                if (isValid) {
                    //noinspection OptionalGetWithoutIsPresent
                    RestaurantEntity currentRestaurantEntity = restaurantEntityOptional.get();
                    int restaurantCapacity = currentRestaurantEntity.getProcessingCapacity();
                    int currRestaurantAssignedQuantity = Math.min(remainingQuantity, restaurantCapacity);
                    assignItemToRestaurant(currentOrderItem, restaurantMenuItem, currentRestaurantEntity,
                            currRestaurantAssignedQuantity, order.getId(), orderItemsList);
                    remainingQuantity -= currRestaurantAssignedQuantity;
                    if (remainingQuantity == 0) {
                        //current item fulfilled and processing capacity locked - go to next item
                        log.info("OrderID: {}, FoodItemId: {} - can be fulfilled completely",
                                order.getId(), currentOrderItem.getFoodItemId());
                        break;
                    }
                } else {
                    log.error("Skipping to next restaurant as validation failed for restaurantId: {}", restaurantMenuItem.getRestaurantId());
                }
            }
            if (remainingQuantity > 0) {
                //TODO: add correlation id for better analysis of logs
                log.error("OrderID: {}, FoodItemId: {}, RequestedQuantity: {} => can't be fulfilled currently. RemainingQuantity : {}",
                        order.getId(), currentOrderItem.getFoodItemId(), currentOrderItem.getQuantity(), remainingQuantity);
                //throw exception to trigger rollback
                throw new OrderNotFulfilledException("Order could not be fulfilled. Try reducing quantity of items or retry after some time.");
            }
        }

        //update order entity - accept order updating price, status and createdOn timestamp
        //save order and order details
        log.info("OrderID: {} - Order Accepted as all items can be fulfilled", order.getId());
        acceptAndSaveOrder(order, orderItemsList);
        return order;
    }

    private OrderEntity initialiseOrderEntity(OrderRequestDTO orderRequestDTO) {
        return OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .deliveryAddress(orderRequestDTO.getDeliveryAddress())
                .userId(orderRequestDTO.getUserId())
                .totalPrice(0.0)
                .receivedAt(Timestamp.from(Instant.now()))
                .status(OrderStatus.PROCESSING.value())
                .build();
    }

    private void assignItemToRestaurant(OrderItemRequestDTO currentOrderItem, MenuItemEntity menuItem, RestaurantEntity restaurantEntity, int assignedQuantity, String orderId, List<OrderItemEntity> orderItemsList) {
        decrementRestaurantCapacity(restaurantEntity, assignedQuantity);
        orderItemsList.add(createOrderItem(orderId, currentOrderItem.getFoodItemId(), assignedQuantity, menuItem));
        log.info("OrderID: {}, FoodItemId: {}, Quantity: {} - fulfilled by RestaurantId: {}",
                orderId, currentOrderItem.getFoodItemId(),
                assignedQuantity, menuItem.getRestaurantId());
    }

    private void decrementRestaurantCapacity(RestaurantEntity restaurantEntity, int decrementCount) {
        restaurantEntity.decrementProcessingCapacity(decrementCount);
        restaurantRepository.save(restaurantEntity);
    }

    private OrderItemEntity createOrderItem(String orderId, String foodItemId, int quantity, MenuItemEntity menuItem) {
        return OrderItemEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderId(orderId)
                .foodItemId(foodItemId)
                .restaurantId(menuItem.getRestaurantId())
                .quantity(quantity)
                .unitPrice(menuItem.getPrice())
                .itemPrice(Double.valueOf(quantity) * menuItem.getPrice())
                .menuItemId(menuItem.getId())
                .status(OrderStatus.ACCEPTED.value())
                .rating(0)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }

    private void acceptAndSaveOrder(OrderEntity order, List<OrderItemEntity> orderItemsList) {
        double orderTotalPrice = calculateOrderTotalPrice(orderItemsList);
        order.updateOrderAsAccepted(orderTotalPrice);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemsList);
        order.addOrderItemsToOrder(orderItemsList);
    }

    //TODO: Index for order_id in order item table
    private double calculateOrderTotalPrice(List<OrderItemEntity> orderItemsList) {
        double totalPrice = 0.0;
        for (OrderItemEntity orderItem : orderItemsList) {
            totalPrice += orderItem.getItemPrice();
        }
        return totalPrice;
    }
}