package com.nishit.FoodOrdering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("delivery_address")
    private String deliveryAddress;

    @JsonProperty("created_on")
    private Timestamp createdOn;

    @JsonProperty("total_price")
    private double totalPrice;

    @JsonProperty("order_items")
    private List<OrderItemDTO> orderItems;
}
