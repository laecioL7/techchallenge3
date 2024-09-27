package com.fiap.techchallenge.restaurant_service.adapter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private String id;
    private String restaurantId;
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationTime;
    private int numberOfPeople;
}
