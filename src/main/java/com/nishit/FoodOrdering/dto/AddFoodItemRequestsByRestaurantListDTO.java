package com.nishit.FoodOrdering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AddFoodItemRequestsByRestaurantListDTO {

    @JsonProperty("restaurant")
    private RestaurantDTO restaurant;

    @JsonProperty("add_food_item_requests")
    private List<AddFoodItemDTO> addFoodItemDTOList;
}
