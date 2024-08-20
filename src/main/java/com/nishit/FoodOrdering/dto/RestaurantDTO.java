package com.nishit.FoodOrdering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RestaurantDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("processing_capacity")
    private Integer processingCapacity;

    @JsonProperty("status")
    private String status;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("address")
    private String address;

    @JsonProperty("menu_items")
    private List<MenuItemDTO> menuItems;

}
