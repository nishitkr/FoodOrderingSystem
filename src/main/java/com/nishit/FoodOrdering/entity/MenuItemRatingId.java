package com.nishit.FoodOrdering.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MenuItemRatingId implements Serializable {

    private String menuItemId;
    private Integer rating;
}
