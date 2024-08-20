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
public class FoodItemDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_on")
    private Timestamp createdOn;

    @JsonProperty("number_of_serving_restaurants")
    private Integer numberOfServingRestaurants;

    @JsonProperty("add_food_item_request_id")
    private String addFoodItemRequestId;
}
