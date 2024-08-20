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
public class OrderRequestDTO {

    @NotBlank(message = "User ID must not be empty or null.")
    @Size(max = 50, message = "The length of user ID must be less than 50.")
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message = "Address must not be empty or null.")
    @Size(max = 1000, message = "The length of address must be less than 1000.")
    @JsonProperty("delivery_address")
    private String deliveryAddress;

    @NotEmpty(message = "The list of items must not be empty")
    @JsonProperty("order_items")
    private List<OrderItemRequestDTO> orderItems;
}
