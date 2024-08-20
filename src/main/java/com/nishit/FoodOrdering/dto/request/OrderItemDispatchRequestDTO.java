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
public class OrderItemDispatchRequestDTO {
    @NotBlank(message = "Restaurant ID must not be empty or null.")
    @Size(max = 50, message = "The length of Restaurant ID must be less than 50.")
    @JsonProperty("restaurant_id")
    private String restaurantId;

    @NotEmpty(message = "The list of Order Item IDs must not be empty")
    @JsonProperty("order_item_id_list")
    private List<String> orderItemIds;
}
