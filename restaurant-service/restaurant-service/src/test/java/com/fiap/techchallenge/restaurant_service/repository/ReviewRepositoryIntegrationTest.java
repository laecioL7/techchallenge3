package com.fiap.techchallenge.restaurant_service.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ReviewRepositoryIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {
        reviewRepository.deleteByUserId("1");
    }


    @Test
    public void testShouldSaveReview() {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("2");
        review.setRating(5);
        review.setComment("Great food!");

        // Act
        Review savedReview = reviewRepository.save(review);

        // Assert
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getUserId()).isEqualTo("1");
    }

    @Test
    public void testShouldFindReviewById() {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("2");
        review.setRating(5);
        review.setComment("Great food!");
        review = reviewRepository.save(review);

        // Act
        Optional<Review> foundReview = reviewRepository.findById(review.getId());

        // Assert
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getUserId()).isEqualTo("1");
    }

    @Test
    public void testShouldUpdateReview() {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("2");
        review.setRating(4);
        review.setComment("Good food!");
        review = reviewRepository.save(review);

        // Act
        review.setRating(5);
        review.setComment("Great food!");
        Review updatedReview = reviewRepository.save(review);

        // Assert
        assertThat(updatedReview).isNotNull();
        assertThat(updatedReview.getRating()).isEqualTo(5);
        assertThat(updatedReview.getComment()).isEqualTo("Great food!");
    }

    @Test
    public void testShouldListAllReviewsByUserId() {
        // Arrange
        String userId = "1";
        Review review1 = new Review();
        review1.setUserId(userId);
        review1.setRestaurantId("2");
        review1.setRating(5);
        review1.setComment("Great food!");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setUserId(userId);
        review2.setRestaurantId("3");
        review2.setRating(4);
        review2.setComment("Good food!");
        reviewRepository.save(review2);

        // Act
        List<Review> foundReviews = reviewRepository.findByUserId(userId);

        // Assert
        assertThat(foundReviews).hasSize(2);
        assertThat(foundReviews).containsExactlyInAnyOrder(review1, review2);
    }

    @Test
    public void testShouldDeleteReviewByUserId() {
        // Arrange
        String userId = "1";
        Review review = new Review();
        review.setUserId(userId);
        review.setRestaurantId("2");
        review.setRating(5);
        review.setComment("Great food!");
        reviewRepository.save(review);

        // Act
        reviewRepository.deleteByUserId(userId);

        // Assert
        List<Review> foundReviews = reviewRepository.findByUserId(userId);
        assertThat(foundReviews).isEmpty();
    }
}