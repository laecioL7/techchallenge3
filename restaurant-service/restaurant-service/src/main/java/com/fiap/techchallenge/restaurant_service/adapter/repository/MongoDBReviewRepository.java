package com.fiap.techchallenge.restaurant_service.adapter.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDBReviewRepository extends MongoRepository<Review, String>, ReviewRepository {
    @Override
    List<Review> findByRestaurantId(String restaurantId);

    @Override
    List<Review> findByUserId(String userId);
}
