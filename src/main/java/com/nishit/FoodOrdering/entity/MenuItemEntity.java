package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.dto.MenuItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "menu")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MenuItemEntity {
    //TODO: Add rating field for HighestRatingRestaurantSelectionStrategy

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "restaurant_id")
    private String restaurantId;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "modified_on")
    private Timestamp modifiedOn;

    @Column(name = "rating")
    private Double rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="food_item_id")
    private FoodItemEntity foodItem;

    public MenuItemDTO convertEntityToDTO() {
        return MenuItemDTO.builder()
                .id(this.getId())
                .foodItemId(this.getFoodItem().getId())
                .foodName(this.getFoodItem().getName())
                .foodDescription(this.getFoodItem().getDescription())
                .foodItemStatus(this.getFoodItem().getStatus())
                .price(this.getPrice())
                .restaurantId(this.getRestaurantId())
                .build();
    }

    public void updateRating(double rating) {
        this.rating = rating;
    }
}
