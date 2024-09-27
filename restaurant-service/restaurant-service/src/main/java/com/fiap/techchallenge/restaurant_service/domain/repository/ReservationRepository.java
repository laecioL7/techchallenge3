package com.fiap.techchallenge.restaurant_service.domain.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(String id);
    List<Reservation> findByRestaurantId(String restaurantId);
    List<Reservation> findByUserId(String userId);
    void deleteByUserId(String userId);
    long count();
    void deleteById(String reservationId);
}
