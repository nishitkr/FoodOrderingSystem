package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
