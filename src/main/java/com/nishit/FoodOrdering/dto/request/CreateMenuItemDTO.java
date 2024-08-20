package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMenuItemDTO {

    @NotBlank(message = "Food Item ID must not be empty or null.")
    @Size(max = 50, message = "The length of food item ID must be less than 50.")
    @JsonProperty("food_item_id")
    private String foodItemId;

    @NotNull(message = "Food price must not be null")
    @Positive(message = "Food item price must be strictly positive")
    @JsonProperty("price")
    private Double price;
}
