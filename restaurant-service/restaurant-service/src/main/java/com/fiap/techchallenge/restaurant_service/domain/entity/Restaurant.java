package com.fiap.techchallenge.restaurant_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String location;
    private String cuisineType;
    private String openingHours;
    private int capacity;
}
