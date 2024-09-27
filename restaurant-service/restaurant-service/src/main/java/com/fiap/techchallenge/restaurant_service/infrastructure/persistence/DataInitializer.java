//package com.fiap.techchallenge.restaurant_service.infrastructure.persistence;
//*** MANTIVE APENAS DE CONSULTA POIS NÃO VI VANTAGEM NOS TESTES
//import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
//import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class DataInitializer {
//    private static final String USER_TEST_ID = "1";
//    private final ReservationRepository reservationRepository;
//
//    @Autowired
//    public DataInitializer(ReservationRepository reservationRepository) {
//        this.reservationRepository = reservationRepository;
//    }
//
//    @PostConstruct
//    public void init() {
//        reservationRepository.deleteByUserId("1");
//
//        Reservation reservation = new Reservation();
//        reservation.setUserId(USER_TEST_ID);
//        reservation.setRestaurantId("2");
//        reservation.setNumberOfPeople(3);
//        reservation.setReservationTime(LocalDateTime.now().plusHours(6));
//        reservationRepository.save(reservation);
//
//        Reservation reservation2 = new Reservation();
//        reservation.setUserId(USER_TEST_ID);
//        reservation2.setRestaurantId("3");
//        reservation2.setNumberOfPeople(2);
//        reservation2.setReservationTime(LocalDateTime.now().plusHours(8));
//        reservationRepository.save(reservation2);
//    }
//}
