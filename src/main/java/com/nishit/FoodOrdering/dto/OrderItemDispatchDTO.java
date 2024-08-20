package com.nishit.FoodOrdering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderItemDispatchDTO {

    @JsonProperty("order_item_id")
    private String orderItemId;

    @JsonProperty("restaurant_id")
    private String restaurantId;

    @JsonProperty("restaurant_name")
    private String restaurantName;

    @JsonProperty("processing_capacity")
    private Integer processingCapacity;

    @JsonProperty("error_description")
    private String errorDescription;
}
