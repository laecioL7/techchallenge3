package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReviewDto;
import com.fiap.techchallenge.restaurant_service.domain.entity.Review;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteByUserId("1");
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testAddReviewSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        restaurant = restaurantRepository.save(restaurant);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUserId("1");
        reviewDto.setRestaurantId(restaurant.getId());
        reviewDto.setRating(5);
        reviewDto.setComment("Great food!");

        // Act & Assert
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.restaurantId").value(restaurant.getId()));
    }

    @Test
    void testAddReviewInvalidData() throws Exception {
        // Arrange
        ReviewDto invalidReviewDto = new ReviewDto();
        invalidReviewDto.setUserId("1");
        invalidReviewDto.setRestaurantId("2");
        invalidReviewDto.setRating(6); // Invalid rating
        invalidReviewDto.setComment("Great food!");

        // Act & Assert
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReviewDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Rating must be between 1 and 5"));
    }

    @Test
    void testGetReviewsByRestaurantSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        restaurant = restaurantRepository.save(restaurant);

        Review review = new Review();
        review.setUserId("1");
        review.setRestaurantId(restaurant.getId());
        review.setRating(5);
        review.setComment("Great food!");
        reviewRepository.save(review);

        // Act & Assert
        mockMvc.perform(get("/reviews")
                        .param("restaurantId", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(review.getId()));
    }

    @Test
    void testGetReviewsByRestaurantFailure() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/reviews")
                        .param("restaurantId", "nonexistentRestaurantId"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No reviews found for the restaurant"));
    }
}