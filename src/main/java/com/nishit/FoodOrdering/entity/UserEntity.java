package com.nishit.FoodOrdering.entity;

import com.nishit.FoodOrdering.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @OneToMany(cascade=CascadeType.PERSIST)
    @JoinColumn(name="user_id")
    private List<OrderEntity> orders;

    public UserDTO convertEntityToDTO() {
        return UserDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .email(this.getEmail())
                .phone(this.getPhone())
                .address(this.getAddress())
                .build();
    }
}
