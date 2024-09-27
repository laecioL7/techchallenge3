package com.fiap.techchallenge.restaurant_service.adapter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private String id;
    private String restaurantId;
    private String userId;
    private int rating;
    private String comment;
    private LocalDateTime reviewTime;
}
