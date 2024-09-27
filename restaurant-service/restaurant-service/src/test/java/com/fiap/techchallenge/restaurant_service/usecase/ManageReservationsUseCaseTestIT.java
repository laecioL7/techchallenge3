package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.ManageReservationsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ManageReservationsUseCaseTestIT {


    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ManageReservationsUseCase manageReservationsUseCase;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteByUserId("user123");
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testViewReservationsByUserByRestaurant() {
        // Arrange
        Restaurant restaurant = restaurantRepository.save(getRestaurant());
        Reservation reservation = getReservation(restaurant);
        reservationRepository.save(reservation);

        // Act
        List<Reservation> reservations = manageReservationsUseCase.viewReservationsByUser(reservation.getUserId());

        // Assert
        assertThat(reservations).isNotEmpty().contains(reservation);
    }

    @Test
    void testThrowExceptionWhenNoReservationsFound() {
        // Act & Assert
        assertThatThrownBy(() -> manageReservationsUseCase.viewReservationsByUser("nonexistentRestaurantId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nenhuma reserva encontrada para o usuário");
    }

    @Test
    void testUpdateReservationSuccess() {
        // Arrange
        Restaurant restaurant = restaurantRepository.save(getRestaurant());
        Reservation reservation = reservationRepository.save(getReservation(restaurant));
        reservation.setNumberOfPeople(4);

        // Act
        manageReservationsUseCase.updateReservation(reservation.getId(),
                reservation.getReservationTime(), reservation.getNumberOfPeople());

        // Assert
        Optional<Reservation> updatedReservation = reservationRepository.findById(reservation.getId());
        assertThat(updatedReservation).isPresent();
        assertThat(updatedReservation.get().getNumberOfPeople()).isEqualTo(4);
    }

    @Test
    void testUpdateReservationFailure() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId("nonexistentReservationId");
        reservation.setReservationTime(LocalDateTime.now().plusDays(1));
        reservation.setNumberOfPeople(2);

        // Act & Assert
        assertThatThrownBy(() -> manageReservationsUseCase.updateReservation(
                reservation.getId(),reservation.getReservationTime(), reservation.getNumberOfPeople()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reserva não encontrada");
    }

    @Test
    void testCancelReservationSuccess() {
        // Arrange
        Restaurant restaurant = restaurantRepository.save(getRestaurant());
        Reservation reservation = reservationRepository.save(getReservation(restaurant));

        // Act
        manageReservationsUseCase.cancelReservation(reservation.getId());

        // Assert
        Optional<Reservation> canceledReservation = reservationRepository.findById(reservation.getId());
        assertThat(canceledReservation).isNotPresent();
    }

    @Test
    void testCancelReservationFailure() {
        // Act & Assert
        assertThatThrownBy(() -> manageReservationsUseCase.cancelReservation("nonexistentReservationId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reserva não encontrada");
    }

    @Test
    void testViewReservationsByRestaurant() {
        // Arrange
        Restaurant restaurant = restaurantRepository.save(getRestaurant());
        Reservation reservation1 = getReservation(restaurant);
        Reservation reservation2 = getReservation(restaurant);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // Act
        List<Reservation> reservations = manageReservationsUseCase.viewReservationsByRestaurant(restaurant.getId());

        // Assert
        assertThat(reservations).isNotEmpty().contains(reservation1, reservation2);
    }

    @Test
    void testViewReservationsByRestaurantFailure() {
        // Act & Assert
        assertThatThrownBy(() -> manageReservationsUseCase.viewReservationsByRestaurant("nonexistentRestaurantId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nenhuma reserva encontrada para o restaurante");
    }

    private Restaurant getRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        return restaurant;
    }

    private Reservation getReservation(Restaurant restaurant) {
        Reservation reservation = new Reservation();
        reservation.setRestaurantId(restaurant.getId());
        reservation.setUserId("user123");
        reservation.setReservationTime(LocalDateTime.now().plusDays(1));
        reservation.setNumberOfPeople(2);
        return reservation;
    }
}