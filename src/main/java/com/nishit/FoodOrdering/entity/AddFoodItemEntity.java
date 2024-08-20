package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.dto.AddFoodItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "add_food_item_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddFoodItemEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "approver_comment")
    private String approverComment;

    @Column(name = "restaurant_id")
    private String restaurantId;

    public AddFoodItemDTO convertEntityToDTO() {
        return AddFoodItemDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .status(this.getStatus())
                .approverComment(this.getApproverComment())
                .restaurantId(this.getRestaurantId())
                .build();
    }

    public void updateApprovalStatusAndComment(String reviewStatus, String approverComment) {
        this.status = reviewStatus;
        this.approverComment = approverComment;
    }
}
