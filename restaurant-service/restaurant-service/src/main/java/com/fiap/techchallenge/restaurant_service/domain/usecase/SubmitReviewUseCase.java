package com.fiap.techchallenge.restaurant_service.domain.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;

import java.time.LocalDateTime;

public class SubmitReviewUseCase {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public SubmitReviewUseCase(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void submitReview(Review review) {
        //verifica se o restaurante existe
        restaurantRepository.findById(review.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        review.setReviewTime(LocalDateTime.now());
        reviewRepository.save(review);
    }
}
