package com.fiap.techchallenge.restaurant_service.adapter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private String id;
    private String restaurantId;
    private String userId;
    private LocalDateTime reservationTime;
    private int numberOfPeople;
}
