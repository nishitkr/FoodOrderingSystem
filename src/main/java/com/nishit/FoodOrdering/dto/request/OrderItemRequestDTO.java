package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemRequestDTO {

    @NotBlank(message = "Food Item ID must not be empty or null.")
    @Size(max = 50, message = "The length of food item ID must be less than 50.")
    @JsonProperty("food_item_id")
    private String foodItemId;

    @NotNull(message = "The quantity of item must not be null")
    @Min(value = 1, message = "The quantity of item to be ordered must be greater than 0")
    private Integer quantity;
}
