package com.fiap.techchallenge.restaurant_service.adapter.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDBRestaurantRepository extends MongoRepository<Restaurant, String>, RestaurantRepository {

    @Query(value = "{ 'location': { $regex: ?0, $options: 'i' }, 'cuisineType': { $regex: ?1, $options: 'i' } }")
    List<Restaurant> findByLocationAndCuisineTypeDesc(String location, String cuisineType);

    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }")
    void deleteByNameContaining(String name);

    @Query(value = "{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Restaurant> findByNameContaining(String name);
}