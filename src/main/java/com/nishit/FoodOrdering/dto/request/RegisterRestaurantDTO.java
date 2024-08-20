package com.nishit.FoodOrdering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRestaurantDTO {

    @NotBlank(message = "Restaurant name must not be empty or null.")
    @Size(max = 100, message = "The length of restaurant name must be less than 100.")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Restaurant tagline must not null.")
    @Size(max = 255, message = "The length of tagline name must be less than 255.")
    @JsonProperty("tagline")
    private String tagline;

    @NotNull(message = "The processing capacity of restaurant must not be null.")
    @Min(value = 1, message = "The processing capacity of restaurant must be greater than 0.")
    @JsonProperty("processing_capacity")
    private Integer processingCapacity;

    @NotBlank(message = "Restaurant address must not be empty or null.")
    @Size(max = 1000, message = "The length of restaurant address must be less than 1000.")
    @JsonProperty("address")
    private String address;
}
