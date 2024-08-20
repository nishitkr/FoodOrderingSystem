package com.nishit.FoodOrdering.domain;

import com.nishit.FoodOrdering.exception.InvalidConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class RestaurantSelectionStrategyFactory {

    @Value("${restaurantSelectionStrategy.name}")
    private String restaurantSelectionStrategyName;

    @Autowired
    private Map<String, RestaurantSelectionStrategy> restaurantSelectionStrategyMap;

    public RestaurantSelectionStrategy getRestaurantSelector() {
        if (restaurantSelectionStrategyMap.containsKey(restaurantSelectionStrategyName)) {
            return restaurantSelectionStrategyMap.get(restaurantSelectionStrategyName);
        } else {
            log.error("Invalid configuration for restaurant selection strategy name");
            throw new InvalidConfigurationException("Invalid configuration for restaurant selection strategy name");
        }
    }
}
