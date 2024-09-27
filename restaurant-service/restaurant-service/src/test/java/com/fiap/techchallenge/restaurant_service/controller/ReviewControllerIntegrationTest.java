package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReviewDto;
import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteByUserId("1");
    }

    @Test
    void testSubmitReviewSuccess() throws Exception {
        // Arrange
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUserId("1");
        reviewDto.setRestaurantId("1");
        reviewDto.setRating(5);
        reviewDto.setComment("Excellent food and service!");

        // Act & Assert
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk());
    }


    @Test
    void testUpdateReviewSuccess() throws Exception {
        // Arrange
        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId("1");
        review.setRating(5);
        review.setComment("Excellent food and service!");
        review = reviewRepository.save(review);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setUserId("1");
        reviewDto.setRestaurantId("1");
        reviewDto.setRating(4);
        reviewDto.setComment("Good food and service!");

        // Act & Assert
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk());
    }
}