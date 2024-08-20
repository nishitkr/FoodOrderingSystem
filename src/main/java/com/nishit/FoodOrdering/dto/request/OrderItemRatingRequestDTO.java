package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemRatingRequestDTO {

    @NotBlank(message = "Order Item ID must not be empty or null.")
    @Size(max = 50, message = "The length of order item ID must be less than 50.")
    @JsonProperty("order_item_id")
    private String orderItemId;

    //TODO: Min and max value validation npt caught by GlobalExceptionHandler
    @NotNull(message = "The rating must not be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Maximum allowed rating is 5")
    private Integer rating;
}
