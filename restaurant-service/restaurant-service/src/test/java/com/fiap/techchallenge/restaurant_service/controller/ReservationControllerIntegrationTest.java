package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReservationDto;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    ReservationRepository reservationRepository;
    
    private String restaurantId = "1";
    private final String userId = "1";
    private String reservationId = "1";

    @BeforeEach
    void setUp() {
        reservationRepository.deleteByUserId(userId);

        List<Restaurant> restList = restaurantRepository.findByNameContaining("Test Restaurant");
        if(restList.isEmpty()){
             restaurantId = saveRestaurant();
        }else {
            restaurantId = restList.get(0).getId();
        }
    }

    @Test
    void testMakeReservationSuccess() throws Exception {
        // Arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId(userId);
        reservationDto.setRestaurantId(restaurantId);
        LocalDateTime reservationTime = LocalDateTime.parse("2024-10-10T19:00:00");
        reservationDto.setReservationTime(reservationTime);
        reservationDto.setNumberOfPeople(4);

        // Act & Assert
        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId))
                .andExpect(jsonPath("$.reservationTime").value("2024-10-10T19:00:00"))
                .andExpect(jsonPath("$.numberOfPeople").value(4));
    }

    @Test
    void testViewReservationsByUserSuccess() throws Exception {
        // Arrange
        saveReservation();

        // Act & Assert
        mockMvc.perform(get("/reservation")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testViewReservationsByRestaurantSuccess() throws Exception {
        // Arrange
        saveReservation();

        // Act & Assert
        mockMvc.perform(get("/reservation/view-reservations-by-restaurants")
                        .param("restaurantId", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testUpdateReservationSuccess() throws Exception {
        // Arrange
        reservationId = saveReservation();

        // Arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservationId);
        reservationDto.setReservationTime(LocalDateTime.parse("2024-10-20T19:00:00"));
        reservationDto.setNumberOfPeople(8);

        // Act & Assert
        mockMvc.perform(put("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelReservationSuccess() throws Exception {
        // Arrange
        reservationId = saveReservation();

        // Act & Assert
        mockMvc.perform(delete("/reservation")
                        .param("reservationId", reservationId))
                .andExpect(status().isOk());
    }

    String saveReservation(){
        return reservationRepository.save(getReservation()).getId();
    }

    String saveRestaurant(){
        return restaurantRepository.save(getRestaurant()).getId();
    }

    Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        return restaurant;
    }

    Reservation getReservation(){
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setRestaurantId(restaurantId);
        reservation.setReservationTime(LocalDateTime.parse("2024-10-10T19:00:00"));
        reservation.setNumberOfPeople(4);
        return reservation;
    }
}
