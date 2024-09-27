package com.fiap.techchallenge.restaurant_service.domain.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;

public class RegisterRestaurantUseCase {

    private final RestaurantRepository restaurantRepository;

    public RegisterRestaurantUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant registerRestaurant(Restaurant restaurant) {
        //se veio id, é update
        return restaurantRepository.save(restaurant);
    }
}
