package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.controller.ReviewController;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReviewDto;
import com.fiap.techchallenge.restaurant_service.adapter.mapper.ReviewMapper;
import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SubmitReviewUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {


    private MockMvc mockMvc;

    @Mock
    private SubmitReviewUseCase submitReviewUseCase;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void testSubmitReviewSuccess() throws Exception {
        // Arrange
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUserId("1");
        reviewDto.setRestaurantId("1");
        reviewDto.setRating(5);
        reviewDto.setComment("Great food!");

        Review review = new Review();
        review.setId("1");
        review.setUserId("1");
        review.setRestaurantId("1");
        review.setRating(5);
        review.setComment("Great food!");

        when(reviewMapper.toEntity(any(ReviewDto.class))).thenReturn(review);
        doNothing().when(submitReviewUseCase).submitReview(any(Review.class));

        // Act & Assert
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reviewDto)))
                .andExpect(status().isOk());
    }
}