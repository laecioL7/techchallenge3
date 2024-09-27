package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SubmitReviewUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
//@Transactional
class SubmitReviewUseCaseTestIT {


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private SubmitReviewUseCase submitReviewUseCase;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteByUserId("1");
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testSubmitReviewSuccess() {
        // Arrange
        Restaurant restaurant = restaurantRepository.save(getRestaurant());

        Review review = getReview(restaurant);

        // Act
        submitReviewUseCase.submitReview(review);

        // Assert
        Optional<Review> savedReview = reviewRepository.findById(review.getId());
        assertThat(savedReview).isPresent();
        assertThat(savedReview.get().getRestaurantId()).isEqualTo(restaurant.getId());
    }

    @Test
    void testSubmitReviewRestaurantNotFound() {
        // Arrange
        Review review = new Review();
        review.setUserId("user123");
        review.setRestaurantId("voidRestaurant");

        // Act & Assert
        assertThatThrownBy(() -> submitReviewUseCase.submitReview(review))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Restaurante não encontrado");
    }

    Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        return restaurant;
    }

    private static Review getReview(Restaurant restaurant) {
        Review review = new Review();
        review.setUserId("user123");
        review.setRestaurantId(restaurant.getId());
        review.setRating(5);
        review.setComment("Comentário");
        review.setReviewTime(LocalDateTime.now());
        return review;
    }
}
