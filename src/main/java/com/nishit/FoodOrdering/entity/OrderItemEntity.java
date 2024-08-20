package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderItemEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "item_price")
    private Double itemPrice;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "food_item_id")
    private String foodItemId;

    @Column(name = "restaurant_id")
    private String restaurantId;

    @Column(name = "status")
    private String status;

    @Column(name = "rating")
    private Integer rating;

    @Column(name="menu_item_id")
    private String menuItemId;

    @Column(name="created_at")
    private Timestamp createdAt;

    @Column(name="dispatched_at")
    private Timestamp dispatchedAt;

    public void dispatchOrderItem() {
        this.status = OrderStatus.DISPATCHED.value();
        this.dispatchedAt = Timestamp.from(Instant.now());
    }

    public void updateRating(int rating) {
        this.rating = rating;
    }
}
