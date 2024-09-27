package com.fiap.techchallenge.restaurant_service.adapter.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RestaurantDTO {
    private String id;
    @NotEmpty
    private String name;
    private String location;
    private String cuisineType;
    private String openingHours;
    private int capacity;
}
