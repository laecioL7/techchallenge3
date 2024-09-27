package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.ManageReservationsUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

class ManageReservationsUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ManageReservationsUseCase manageReservationsUseCase;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        openMocks.close();
    }


    @Test
    void testViewReservationsByUser() {
        // Arrange
        String userId = "1";
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        when(reservationRepository.findByUserId(userId)).thenReturn(Collections.singletonList(reservation));

        // Act
        List<Reservation> reservations = manageReservationsUseCase.viewReservationsByUser(userId);

        // Assert
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.get(0).getUserId()).isEqualTo(userId);

        /*O verify do Mockito é usado para verificar se o método findByUserId
         foi chamado exatamente uma vez com o argumento userId. Pra garantir
         que o método viewReservations está funcionando conforme o esperado, delegando
          a tarefa de buscar reservas para a camada de repositório.*/
        verify(reservationRepository).findByUserId(userId);
    }

    @Test
    void testThrowExceptionWhenNoReservationsFound() {
        // Arrange
        String userId = "1";
        when(reservationRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        // Act
        Throwable throwable = catchThrowable(() -> manageReservationsUseCase.viewReservationsByUser(userId));
        // Assert
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Nenhuma reserva encontrada para o usuário");
        verify(reservationRepository).findByUserId(userId);
    }

    @Test
    void testUpdateReservationSuccess() {
        // Arrange
        String reservationId = "1";
        LocalDateTime reservationTime = LocalDateTime.now();
        int numberOfPeople = 4;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        manageReservationsUseCase.updateReservation(reservationId, reservationTime, numberOfPeople);

        // Assert
        assertThat(reservation.getReservationTime()).isEqualTo(reservationTime);
        assertThat(reservation.getNumberOfPeople()).isEqualTo(numberOfPeople);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void testUpdateReservationFailure() {
        // Arrange
        String reservationId = "1";
        LocalDateTime reservationTime = LocalDateTime.now();
        int numberOfPeople = 4;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Throwable throwable = catchThrowable(() -> manageReservationsUseCase.updateReservation(reservationId, reservationTime, numberOfPeople));

        // Assert
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Reserva não encontrada");
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void testCancelReservationSuccess() {
        // Arrange
        String reservationId = "1";
        Reservation reservation = new Reservation();
        reservation.setId("1");
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).deleteById(reservation.getId());

        // Act
        manageReservationsUseCase.cancelReservation(reservationId);

        // Assert
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).deleteById(reservationId);
    }

    @Test
    void testCancelReservationFailure() {
        // Arrange
        String reservationId = "1";
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Throwable throwable = catchThrowable(() -> manageReservationsUseCase.cancelReservation(reservationId));

        // Assert
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class).hasMessage("Reserva não encontrada");
        verify(reservationRepository).findById(reservationId);
    }
}