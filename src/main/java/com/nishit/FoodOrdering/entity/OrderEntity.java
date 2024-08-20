package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status")
    private String status;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "received_on")
    private Timestamp receivedAt;

    @Column(name = "created_on")
    private Timestamp createdAt;

    @Column(name = "dispatched_at")
    private Timestamp dispatchedAt;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_price")
    private double totalPrice;

    @OneToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name="order_id")
    private List<OrderItemEntity> orderItems;

    public void updateOrderAsAccepted(double totalPrice) {
        this.totalPrice = totalPrice;
        this.status = OrderStatus.ACCEPTED.value();
        this.createdAt = Timestamp.from(Instant.now());
    }

    public void addOrderItemsToOrder(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public void updateOrderAsDispatched() {
        this.status = OrderStatus.DISPATCHED.value();
        this.dispatchedAt = Timestamp.from(Instant.now());
    }
}
