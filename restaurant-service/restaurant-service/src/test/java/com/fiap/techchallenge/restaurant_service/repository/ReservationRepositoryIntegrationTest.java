package com.fiap.techchallenge.restaurant_service.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@ActiveProfiles("test")
class ReservationRepositoryIntegrationTest {

    static final String USER_TEST_ID = "1";

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setUp() {
        reservationRepository.deleteByUserId("1");
    }

    @Test
    public void testShoudCreateTable(){
        // Arrange
        Reservation reservation = getReservation();
        reservationRepository.save(reservation);

        //act
        var count = reservationRepository.count();

        // Assert
        assertThat(count).isGreaterThan(0);
    }

    @Test
    public void testShouldSaveReservation() {
        // Arrange
        Reservation reservation = getReservation();

        // Act
        Reservation savedReservation = reservationRepository.save(reservation);

        // Assert
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getUserId()).isEqualTo("1");
    }

    @Test
    public void testShouldFindReservationById() {
        // Arrange
        Reservation reservation = getReservation();
        reservation = reservationRepository.save(reservation);

        // Act
        Optional<Reservation> foundReservation = reservationRepository.findById(reservation.getId());

        // Assert
        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getUserId()).isEqualTo("1");
    }

    @Test
    public void testShouldUpdateReservation() {
        // Arrange
        Reservation reservation = getReservation();
        reservation = reservationRepository.save(reservation);

        // Act
        reservation.setNumberOfPeople(4);
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Assert
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getNumberOfPeople()).isEqualTo(4);
    }

    @Test
    void testShouldListAllReservationsByUserId() {
        // Arrange
        Reservation reservation1 = getReservation();
        reservation1 = reservationRepository.save(reservation1);

        Reservation reservation2 = getReservation();
        reservation2.setRestaurantId("3");
        reservation2.setNumberOfPeople(2);
        reservation2.setReservationTime(LocalDateTime.now().plusHours(8));
        reservation2 = reservationRepository.save(reservation2);

        // Act
        List<Reservation> foundReservations = reservationRepository.findByUserId(USER_TEST_ID);

        // Assert
        assertThat(foundReservations).hasSize(2);
        //não funciona mesmo os objetos sendo 100% iguais por conta da data
        //foi necessário sobreescrever o método equals na classe Reservation
        assertThat(foundReservations).containsExactlyInAnyOrder(reservation1, reservation2);
    }

    @Test
    public void testShouldDeleteReservationByUserId() {
        // Arrange
        Reservation reservation = getReservation();
        reservationRepository.save(reservation);

        // Act
        reservationRepository.deleteByUserId(USER_TEST_ID);

        // Assert
        List<Reservation> foundReservations = reservationRepository.findByUserId(USER_TEST_ID);
        assertThat(foundReservations).isEmpty();
    }

    // Arrange the Reservation object
    private static Reservation getReservation() {
        Reservation reservation = new Reservation();
        reservation.setUserId(USER_TEST_ID);
        reservation.setRestaurantId("2");
        reservation.setNumberOfPeople(3);
        reservation.setReservationTime(LocalDateTime.now().plusHours(6));
        return reservation;
    }

}