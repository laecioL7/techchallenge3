package com.fiap.techchallenge.restaurant_service.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationRepositoryTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationRepositoryTest reservationRepositoryTest;

    AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() throws Exception {
        openMocks.close();
    }

    @Test
    public void testShoudSaveReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId("2");
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation savedReservation = reservationRepository.save(reservation);

        // Assert
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getUserId()).isEqualTo("1");

        // Verify that the save method was called at least once
        verify(reservationRepository, times(1))
                .save(any(Reservation.class));
    }

    @Test
    public void testShouldFindReservationById() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId("2");
        reservation.setNumberOfPeople(30);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));

        when(reservationRepository.findById(any(String.class))).thenReturn(Optional.of(reservation));

        // Act
        Optional<Reservation> foundReservation = reservationRepository.findById("1");

        // Assert
        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getUserId()).isEqualTo("1");

        // Verify that the findById method was called at least once
        verify(reservationRepository, times(1)).findById(any(String.class));
    }

    @Test
    public void testShouldUpdateReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setUserId("1");
        reservation.setRestaurantId("2");
        reservation.setNumberOfPeople(3);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        reservation.setNumberOfPeople(4);
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Assert
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getNumberOfPeople()).isEqualTo(4);

        // Verify that the save method was called at least once
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testShouldListAllReservationsByUserId() {
        // Arrange
        String userId = "1";
        Reservation reservation1 = new Reservation();
        reservation1.setUserId(userId);
        reservation1.setRestaurantId("2");
        reservation1.setNumberOfPeople(30);
        reservation1.setReservationTime(LocalDateTime.now().plusHours(6));

        Reservation reservation2 = new Reservation();
        reservation2.setUserId(userId);
        reservation2.setRestaurantId("3");
        reservation2.setNumberOfPeople(20);
        reservation2.setReservationTime(LocalDateTime.now().plusHours(8));

        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationRepository.findByUserId(userId)).thenReturn(reservations);

        // Act
        List<Reservation> foundReservations = reservationRepository.findByUserId(userId);

        // Assert
        assertThat(foundReservations).hasSize(2);
        assertThat(foundReservations).contains(reservation1, reservation2);

        // Verify that the findByUserId method was called at least once
        verify(reservationRepository, times(1)).findByUserId(userId);
    }
}
