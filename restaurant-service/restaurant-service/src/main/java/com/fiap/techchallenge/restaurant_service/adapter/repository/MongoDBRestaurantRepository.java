package com.fiap.techchallenge.restaurant_service.adapter.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDBRestaurantRepository extends MongoRepository<Restaurant, String>, RestaurantRepository {
    @Override
    List<Restaurant> findByLocationAndCuisineType(String location, String cuisineType);

    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }", delete = true)
    void deleteByNameContaining(String name);

    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }", delete = true)
    List<Restaurant> findByNameContaining(String name);
}