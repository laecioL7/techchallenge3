package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SubmitReviewUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SubmitReviewUseCaseTest {

    private static final String RESTAURANT_TEST_ID = "restaurant123";

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurepository;

    @InjectMocks
    private SubmitReviewUseCase submitReviewUseCase;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        openMocks.close();
    }

    @Test
    void testSubmitReviewSuccess() {
        // Arrange
        Review review = getReview();

        //se n der erro é pq o restaurante existe
        when(restaurepository.findById(RESTAURANT_TEST_ID)).thenReturn(Optional.of(new Restaurant()));

        // Act
        submitReviewUseCase.submitReview(review);

        // Assert
        verify(restaurepository, times(1)).findById(RESTAURANT_TEST_ID);
        verify(reviewRepository, times(1)).save(review);
    }

    private static Review getReview() {
        Review review = new Review();
        review.setUserId("user123");
        review.setRestaurantId(RESTAURANT_TEST_ID);
        review.setRating(5);
        review.setComment("Comentário");
        review.setReviewTime(LocalDateTime.now());
        return review;
    }


    @Test
    void testSubmitReviewRestaurantNotFound() {
        // Arrange
        Review review = new Review();
        review.setUserId("user123");
        review.setRestaurantId("voidRestaurant");

        when(restaurepository.findById("voidRestaurant")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> submitReviewUseCase.submitReview(review))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Restaurante não encontrado");
    }
}