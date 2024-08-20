package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.dto.RestaurantDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RestaurantEntity {

    //TODO: Add column createdOn and modifiedOn

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "processing_capacity")
    private Integer processingCapacity;

    @Column(name = "status")
    private String status;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "address")
    private String address;

    @Column(name = "approver_comment")
    private String approverComment;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    private List<MenuItemEntity> menuItems;

    public RestaurantDTO convertEntityToDTO() {
        return RestaurantDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .processingCapacity(this.getProcessingCapacity())
                .status(this.getStatus())
                .rating(this.getRating())
                .tagline(this.getTagline())
                .address(this.getAddress())
                .menuItems(
                        Objects.isNull(this.menuItems)
                                ? new ArrayList<>()
                                : this.menuItems.stream()
                        .map(MenuItemEntity::convertEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public void updateApprovalStatusAndComment(String approvalStatus, String approverComment) {
        this.status = approvalStatus;
        this.approverComment = approverComment;
    }

    public void addMenuItems(List<MenuItemEntity> menuItems) {
        this.menuItems = menuItems;
    }

    public void decrementProcessingCapacity(int decrementCount) {
        this.processingCapacity -= decrementCount;
    }

    public void incrementProcessingCapacity(int incrementCount) {
        this.processingCapacity += incrementCount;
    }
}
