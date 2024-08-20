package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRatingRequestDTO {

    @NotBlank(message = "Order ID must not be empty or null.")
    @Size(max = 50, message = "The length of order ID must be less than 50.")
    @JsonProperty("order_id")
    private String orderId;

    @NotEmpty(message = "Order Items rating list must not be empty")
    @JsonProperty("order_items_rating")
    private List<OrderItemRatingRequestDTO> orderItemsRating;
}
