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
public class OrderItemDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("unit_price")
    private Double unitPrice;

    @JsonProperty("item_price")
    private Double itemPrice;

    @JsonProperty("food_item_id")
    private String foodItemId;

    @JsonProperty("food_item_name")
    private String foodItemName;

    @JsonProperty("restaurant_id")
    private String restaurantId;

    @JsonProperty("restaurant_name")
    private String restaurantName;

    @JsonProperty("status")
    private String status;

    @JsonProperty("rating")
    private Integer rating;
}
