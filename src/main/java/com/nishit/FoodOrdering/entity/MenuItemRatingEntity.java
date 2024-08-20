package com.nishit.FoodOrdering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_item_rating")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@IdClass(MenuItemRatingId.class)
public class MenuItemRatingEntity {

    @Id
    @Column(name = "menu_item_id")
    private String menuItemId;

    @Id
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "rating_count")
    private Integer ratingCount;

    public void incrementRatingCount() {
        this.ratingCount = this.ratingCount + 1;
    }
}
