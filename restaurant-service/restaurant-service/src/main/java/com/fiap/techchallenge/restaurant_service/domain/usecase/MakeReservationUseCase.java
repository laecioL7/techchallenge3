package com.fiap.techchallenge.restaurant_service.domain.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;

public class MakeReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;

    public MakeReservationUseCase(ReservationRepository reservationRepository1, RestaurantRepository restaurantRepository) {
        this.reservationRepository = reservationRepository1;
        this.restaurantRepository = restaurantRepository;
    }

    public Reservation makeReservation(Reservation reservation) {
        //verifica se a data de reserva é valida e se tem pelo menos 2 horas de antecedência
        if (reservation.getReservationTime().isBefore(java.time.LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Hora de reserva inválida, deve ter no mínimo 2 horas de antecedência");
        }

        //verifica se o restaurante é válido
        restaurantRepository.findById(reservation.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        return reservationRepository.save(reservation);
    }
}
