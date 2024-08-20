package com.nishit.FoodOrdering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MenuItemDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("food_name")
    private String foodName;

    @JsonProperty("food_description")
    private String foodDescription;

    @JsonProperty("food_item_status")
    private String foodItemStatus;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("food_item_id")
    private String foodItemId;

    @JsonProperty("restaurant_id")
    private String restaurantId;
}
