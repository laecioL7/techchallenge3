package com.fiap.techchallenge.restaurant_service.adapter.controller;

import com.fiap.techchallenge.restaurant_service.adapter.dto.ReservationDto;
import com.fiap.techchallenge.restaurant_service.adapter.mapper.ReservationMapper;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.usecase.MakeReservationUseCase;
import com.fiap.techchallenge.restaurant_service.domain.usecase.ManageReservationsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final MakeReservationUseCase makeReservationUseCase;
    private final ManageReservationsUseCase manageReservationsUseCase;

    private final ReservationMapper reservationMapper;

    public ReservationController(MakeReservationUseCase makeReservationUseCase,
                                 ManageReservationsUseCase manageReservationsUseCase, ReservationMapper reservationMapper) {
        this.makeReservationUseCase = makeReservationUseCase;
        this.manageReservationsUseCase = manageReservationsUseCase;
        this.reservationMapper = reservationMapper;
    }

    @PostMapping
    public ResponseEntity<ReservationDto> makeReservation(@RequestBody ReservationDto reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = makeReservationUseCase.makeReservation(reservation);
        return ResponseEntity.ok(reservationMapper.toDTO(reservation));
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> viewReservationsByUser(@RequestParam String userId) {
        return ResponseEntity.ok(manageReservationsUseCase.viewReservationsByUser(userId));
    }

    @GetMapping("/view-reservations-by-restaurants")
    public ResponseEntity<List<Reservation>> viewReservationsByRestaurant(@RequestParam String restaurantId) {
        return ResponseEntity.ok(manageReservationsUseCase.viewReservationsByRestaurant(restaurantId));
    }

    @PutMapping
    public void updateReservation(@RequestBody ReservationDto reservationDTO) {
        manageReservationsUseCase.updateReservation(reservationDTO.getId(),
                reservationDTO.getReservationTime(), reservationDTO.getNumberOfPeople());
    }

    @DeleteMapping
    public void cancelReservation(@RequestParam String reservationId) {
        manageReservationsUseCase.cancelReservation(reservationId);
    }

}
