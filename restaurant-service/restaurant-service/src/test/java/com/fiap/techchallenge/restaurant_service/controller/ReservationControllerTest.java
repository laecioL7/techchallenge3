package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.techchallenge.restaurant_service.adapter.controller.ReservationController;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReservationDto;
import com.fiap.techchallenge.restaurant_service.adapter.mapper.ReservationMapper;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.usecase.MakeReservationUseCase;
import com.fiap.techchallenge.restaurant_service.domain.usecase.ManageReservationsUseCase;
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

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {
    private MockMvc mockMvc;

    @Mock
    private MakeReservationUseCase makeReservationUseCase;

    @Mock
    private ManageReservationsUseCase manageReservationsUseCase;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationController reservationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testMakeReservationSuccess() throws Exception {
        // Arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId("1");
        reservationDto.setRestaurantId("1");
        //criar um localDateTime com uma data e hora especifica
        LocalDateTime reservationTime = LocalDateTime.of(2023, 10, 10, 19, 0);
        reservationDto.setReservationTime(reservationTime);
        reservationDto.setNumberOfPeople(4);

        Reservation reservation = getReservation();

        when(reservationMapper.toEntity(any(ReservationDto.class))).thenReturn(reservation);
        when(makeReservationUseCase.makeReservation(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(reservationDto);

        // Act & Assert
        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testViewReservationsByUserSuccess() throws Exception {
        // Arrange
        Reservation reservation = getReservation();

        when(manageReservationsUseCase.viewReservationsByUser("1"))
                .thenReturn(Collections.singletonList(reservation));

        // Act & Assert
        mockMvc.perform(get("/reservation")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testViewReservationsByRestaurantSuccess() throws Exception {
        // Arrange
        Reservation reservation = getReservation();

        when(manageReservationsUseCase.viewReservationsByRestaurant("1"))
                .thenReturn(Collections.singletonList(reservation));

        // Act & Assert
        mockMvc.perform(get("/reservation/view-reservations-by-restaurants")
                        .param("restaurantId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateReservationSuccess() throws Exception {
        // Arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId("1");
        reservationDto.setReservationTime(LocalDateTime.parse("2023-10-10T19:00:00"));
        reservationDto.setNumberOfPeople(4);

        doNothing().when(manageReservationsUseCase).updateReservation(any(String.class), any(LocalDateTime.class), any(Integer.class));

        // Act & Assert
        mockMvc.perform(put("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelReservationSuccess() throws Exception {
        // Arrange
        doNothing().when(manageReservationsUseCase).cancelReservation("1");

        // Act & Assert
        mockMvc.perform(delete("/reservation")
                        .param("reservationId", "1"))
                .andExpect(status().isOk());
    }

    private static Reservation getReservation() {
        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setUserId("1");
        reservation.setRestaurantId("1");
        reservation.setReservationTime(LocalDateTime.parse("2023-10-10T19:00:00"));
        reservation.setNumberOfPeople(4);
        return reservation;
    }
}