package com.fiap.techchallenge.restaurant_service.adapter.mapper;

import com.fiap.techchallenge.restaurant_service.adapter.dto.ReservationDto;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);
    ReservationDto toDTO(Reservation reservation);
    Reservation toEntity(ReservationDto reservationDto);
}