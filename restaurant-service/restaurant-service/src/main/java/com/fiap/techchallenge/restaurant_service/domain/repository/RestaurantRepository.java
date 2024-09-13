package com.fiap.techchallenge.restaurant_service.domain.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(String id);
    List<Restaurant> findAll();
    List<Restaurant> findByLocationAndCuisineType(String location, String cuisineType);
    long count();
    void deleteById(String id);
    void deleteByNameContaining(String name);
    List<Restaurant> findByNameContaining(String name);
}
