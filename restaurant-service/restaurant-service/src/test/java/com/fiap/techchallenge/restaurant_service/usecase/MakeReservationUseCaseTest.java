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

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MakeReservationUseCaseTest {

    private MakeReservationUseCase makeReservationUseCase;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        makeReservationUseCase = new MakeReservationUseCase(reservationRepository, restaurantRepository);
    }

    @AfterEach
    void close() throws Exception {
        openMocks.close();
    }

    @Test
    void testMakeReservation_Success() {
        //padrão = Arrange - Act - Assert
        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId("2");
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));

        when(restaurantRepository.findById("2")).thenReturn(java.util.Optional.of(new Restaurant()));

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        //act
        Reservation savedReservation = makeReservationUseCase.makeReservation(reservation);

        // Assert
        //verifica se o método save foi chamado e se o findById foi chamado
        verify(reservationRepository).save(reservation);
        verify(restaurantRepository).findById("2");

        assertThat(savedReservation)
                .isInstanceOf(Reservation.class)
                .isNotNull();
    }

    @Test
    void testMakeExceptionWhenReservation_InvalidData() {
        // Arrange
        Reservation invalidReservation = new Reservation(/* parâmetros inválidos */);
        invalidReservation.setReservationTime(LocalDateTime.now());

        // Act & Assert
        Throwable thrown = catchThrowable(() -> {
            makeReservationUseCase.makeReservation(invalidReservation);
        });

        // Assert
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hora de reserva inválida, deve ter no mínimo 2 horas de antecedência");
    }

    @Test
    public void testMakeExceptionWhenReservation_InvalidRestaurant() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId("-1");
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(24));

        // Act & Assert
        Throwable thrown = catchThrowable(() -> {
            makeReservationUseCase.makeReservation(reservation);
        });

        // Assert
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Restaurante não encontrado");
    }

    //assertTrue / assertFalse

}
