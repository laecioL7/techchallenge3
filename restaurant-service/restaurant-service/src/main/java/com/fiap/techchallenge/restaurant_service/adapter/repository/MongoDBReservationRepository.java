package com.fiap.techchallenge.restaurant_service.adapter.repository;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDBReservationRepository extends MongoRepository<Reservation, String>, ReservationRepository {
    @Override
    List<Reservation> findByRestaurantId(String restaurantId);

    @Override
    List<Reservation> findByUserId(String userId);

    void deleteByUserId(String userId);
}