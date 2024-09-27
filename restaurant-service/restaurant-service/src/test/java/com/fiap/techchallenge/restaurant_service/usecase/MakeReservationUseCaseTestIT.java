package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.MakeReservationUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MakeReservationUseCaseTestIT {

    @Autowired
    private MakeReservationUseCase makeReservationUseCase;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @BeforeEach
    void setUp() {
        reservationRepository.deleteByUserId("1");
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testMakeReservationSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        restaurant = restaurantRepository.save(restaurant);

        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId(restaurant.getId());
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));

        // Act
        Reservation savedReservation = makeReservationUseCase.makeReservation(reservation);

        // Assert
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getId()).isNotNull();
        assertThat(savedReservation.getRestaurantId()).isEqualTo(restaurant.getId());
    }

    @Test
    void testMakeReservationInvalidData() {
        // Arrange
        Reservation invalidReservation = new Reservation();
        invalidReservation.setUserId("1");
        invalidReservation.setRestaurantId("2");
        invalidReservation.setNumberOfPeople(30);
        invalidReservation.setReservationTime(LocalDateTime.now());

        // Act & Assert
        Throwable thrown = catchThrowable(() -> makeReservationUseCase.makeReservation(invalidReservation));

        // Assert
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hora de reserva inválida, deve ter no mínimo 2 horas de antecedência");
    }

    @Test
    void testMakeReservationInvalidRestaurant() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId("-1");
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(24));

        // Act & Assert
        Throwable thrown = catchThrowable(() -> makeReservationUseCase.makeReservation(reservation));

        // Assert
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Restaurante não encontrado");
    }

}