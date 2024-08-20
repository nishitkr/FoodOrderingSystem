package com.nishit.FoodOrdering.repository;

import com.nishit.FoodOrdering.entity.RestaurantEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "SELECT res FROM RestaurantEntity res WHERE res.id = :restaurantId")
    Optional<RestaurantEntity> findByIdWithPessimisticReadLock(@Param("restaurantId") String restaurantId);

}
