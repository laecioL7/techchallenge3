package com.fiap.techchallenge.restaurant_service.domain.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ManageReservationsUseCase {
    private ReservationRepository reservationRepository;

    public ManageReservationsUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> viewReservationsByUser(String userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        if(reservations.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma reserva encontrada para o usuário");
        }
        return reservations;
    }

    public void updateReservation(String reservationId, LocalDateTime reservationTime,  int numberOfPeople) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));
        reservation.setReservationTime(reservationTime);
        reservation.setNumberOfPeople(numberOfPeople);
        reservationRepository.save(reservation);
    }

    public void cancelReservation(String reservationId) {
        reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));
        reservationRepository.deleteById(reservationId);
    }

    public List<Reservation> viewReservationsByRestaurant(String restaurantId) {
        List<Reservation> reservations = reservationRepository.findByRestaurantId(restaurantId);
        if(reservations.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma reserva encontrada para o restaurante");
        }
        return reservations;
    }
}
