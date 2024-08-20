package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddFoodItemRequestDTO {

    @NotBlank(message = "Restaurant ID must not be empty or null.")
    @Size(max = 50, message = "The length of restaurant ID must be less than 50.")
    @JsonProperty("restaurant_id")
    private String restaurantId;

    @NotBlank(message = "Food name must not be empty or null.")
    @Size(max = 100, message = "The length of name must be less than 100.")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Food description must not be empty or null.")
    @Size(max = 255, message = "The length of description must be less than 255.")
    @JsonProperty("description")
    private String description;
}
