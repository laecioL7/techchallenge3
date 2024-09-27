package com.fiap.techchallenge.restaurant_service.infrastructure.configuration;

import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReviewRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    // Constructor Injection
    public UseCaseConfig(RestaurantRepository restaurantRepository,
                         ReservationRepository reservationRepository,
                         ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
    }

    @Bean
    public SubmitReviewUseCase submitReviewUseCase() {
        return new SubmitReviewUseCase(reviewRepository, restaurantRepository);
    }

    @Bean
    public MakeReservationUseCase makeReservationUseCase() {
        return new MakeReservationUseCase(reservationRepository, restaurantRepository);
    }

    @Bean
    public ManageReservationsUseCase manageReservationsUseCase() {
        return new ManageReservationsUseCase(reservationRepository);
    }

    @Bean
    public RegisterRestaurantUseCase registerRestaurantUseCase() {
        return new RegisterRestaurantUseCase(restaurantRepository);
    }

    @Bean
    public SearchRestaurantsUseCase searchRestaurantsUseCase() {
        return new SearchRestaurantsUseCase(restaurantRepository);
    }
}