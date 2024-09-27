package com.fiap.techchallenge.restaurant_service.adapter.mapper;

import com.fiap.techchallenge.restaurant_service.adapter.dto.RestaurantDTO;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);
    RestaurantDTO toDTO(Restaurant restaurant);
    Restaurant toEntity(RestaurantDTO restaurantDTO);
}