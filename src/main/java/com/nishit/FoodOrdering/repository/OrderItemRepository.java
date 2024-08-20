package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, String> {

    List<OrderItemEntity> findAllByOrderId(String orderId);
}
