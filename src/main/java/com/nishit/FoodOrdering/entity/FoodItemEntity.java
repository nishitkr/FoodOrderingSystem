package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.dto.FoodItemDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "food_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FoodItemEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "number_of_serving_restaurants")
    private Integer numberOfServingRestaurants;

    @Column(name = "add_food_item_request_id")
    private String addFoodItemRequestId;

    @OneToMany(mappedBy = "foodItem", fetch = FetchType.LAZY)
    private List<MenuItemEntity> menuItems;

    public FoodItemDTO convertEntityToDTO() {
        return FoodItemDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .status(this.getStatus())
                .createdOn(this.getCreatedOn())
                .numberOfServingRestaurants(this.getNumberOfServingRestaurants())
                .addFoodItemRequestId(this.getAddFoodItemRequestId())
                .build();
    }

    public void incrementNumberOfServingRestaurantByOne() {
        this.numberOfServingRestaurants++;
    }
}
