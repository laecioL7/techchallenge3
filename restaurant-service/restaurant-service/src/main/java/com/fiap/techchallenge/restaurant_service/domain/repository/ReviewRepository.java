package com.fiap.techchallenge.restaurant_service.domain.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(String id);
    List<Review> findByRestaurantId(String restaurantId);
    List<Review> findByUserId(String userId);
    long count();
    void deleteByUserId(String userId);
}
