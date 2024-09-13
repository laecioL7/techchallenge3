package com.fiap.techchallenge.restaurant_service.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewRepositoryTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewRepositoryTest reviewRepositoryTest;

    AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() throws Exception {
        openMocks.close();
    }

    @Test
    public void testShouldSaveReview() {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("2");
        review.setRating(5);
        review.setComment("Great food!");

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        Review savedReview = reviewRepository.save(review);

        // Assert
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getUserId()).isEqualTo("1");

        // Verify that the save method was called at least once
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void testShouldFindReviewById() {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("2");
        review.setRating(5);
        review.setComment("Great food!");

        when(reviewRepository.findById(any(String.class))).thenReturn(Optional.of(review));

        // Act
        Optional<Review> foundReview = reviewRepository.findById("1");

        // Assert
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getUserId()).isEqualTo("1");

        // Verify that the findById method was called at least once
        verify(reviewRepository, times(1)).findById(any(String.class));
    }

    @Test
    public void testShouldUpdateReview() {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("2");
        review.setRating(4);
        review.setComment("Good food!");

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        review.setRating(5);
        review.setComment("Great food!");
        Review updatedReview = reviewRepository.save(review);

        // Assert
        assertThat(updatedReview).isNotNull();
        assertThat(updatedReview.getRating()).isEqualTo(5);
        assertThat(updatedReview.getComment()).isEqualTo("Great food!");

        // Verify that the save method was called at least once
        verify(reviewRepository, times(1)).save(any(Review.class));
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

        Review review2 = new Review();
        review2.setUserId(userId);
        review2.setRestaurantId("3");
        review2.setRating(4);
        review2.setComment("Good food!");

        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewRepository.findByUserId(userId)).thenReturn(reviews);

        // Act
        List<Review> foundReviews = reviewRepository.findByUserId(userId);

        // Assert
        assertThat(foundReviews).hasSize(2);
        assertThat(foundReviews).contains(review1, review2);

        // Verify that the findByUserId method was called at least once
        verify(reviewRepository, times(1)).findByUserId(userId);
    }
}