package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.RegisterRestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class RegisterRestaurantUseCaseTestIT {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RegisterRestaurantUseCase registerRestaurantUseCase;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testRegisterRestaurantSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);

        // Act
        registerRestaurantUseCase.registerRestaurant(restaurant);

        // Assert
        Optional<Restaurant> savedRestaurant = restaurantRepository.findById(restaurant.getId());
        assertThat(savedRestaurant).isPresent();
        assertThat(savedRestaurant.get().getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void testUpdateRestaurantSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setName("Test Restaurant Updated");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italiana");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(200);
        restaurantRepository.save(restaurant);

        // Act
        registerRestaurantUseCase.registerRestaurant(restaurant);

        // Assert
        Optional<Restaurant> savedRestaurant = restaurantRepository.findById(restaurant.getId());
        assertThat(savedRestaurant).isPresent();
        assertThat(savedRestaurant.get().getName()).isEqualTo("Test Restaurant Updated");
        assertThat(savedRestaurant.get().getCapacity()).isEqualTo(200);
    }
}