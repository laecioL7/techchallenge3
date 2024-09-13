package com.fiap.techchallenge.restaurant_service.controller;

import com.fiap.techchallenge.restaurant_service.adapter.dto.ReservationDto;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteByUserId("1");
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testMakeReservationSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        restaurant = restaurantRepository.save(restaurant);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId("1");
        reservationDto.setRestaurantId(restaurant.getId());
        reservationDto.setNumberOfPeople(30);
        reservationDto.setReservationTime(LocalDateTime.now().plusHours(6));

        // Act & Assert
        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.restaurantId").value(restaurant.getId()));
    }

    @Test
    void testMakeReservationInvalidData() throws Exception {
        // Arrange
        ReservationDto invalidReservationDto = new ReservationDto();
        invalidReservationDto.setUserId("1");
        invalidReservationDto.setRestaurantId("2");
        invalidReservationDto.setNumberOfPeople(30);
        invalidReservationDto.setReservationTime(LocalDateTime.now());

        // Act & Assert
        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReservationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testViewReservationsByRestaurantSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        restaurant = restaurantRepository.save(restaurant);

        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId(restaurant.getId());
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));
        reservationRepository.save(reservation);

        // Act & Assert
        mockMvc.perform(get("/reservation/view-reservations-by-restaurants")
                        .param("restaurantId", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()));
    }

    @Test
    void testViewReservationsByRestaurantFailure() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/reservation/view-reservations-by-restaurants")
                        .param("restaurantId", "nonexistentRestaurantId"))
                .andExpect(status().isBadRequest());
    }
}