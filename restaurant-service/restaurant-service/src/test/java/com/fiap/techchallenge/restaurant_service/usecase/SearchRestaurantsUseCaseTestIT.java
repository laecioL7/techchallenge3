package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.exceptions.NoResultsFoundException;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SearchRestaurantsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SearchRestaurantsUseCaseTestIT {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private SearchRestaurantsUseCase searchRestaurantsUseCase;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteByNameContaining("Test");
    }

    @Test
    void testSearchRestaurantsByLocationAndCuisineTypeSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        restaurant.setCuisineType("Test Cuisine");
        restaurantRepository.save(restaurant);

        // Act
        List<Restaurant> result = searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType("Test Location", "Test Cuisine");

        // Assert
        assertThat(result).isNotEmpty().contains(restaurant);
    }

    @Test
    void testSearchRestaurantsByNameSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant ABC");
        restaurant.setLocation("Acre");
        restaurant.setCuisineType("PADARIAS");
        restaurantRepository.save(restaurant);

        // Act
        List<Restaurant> result = searchRestaurantsUseCase.searchRestaurantsByName("Test Restaurant");

        // Assert
        assertThat(result).isNotEmpty().contains(restaurant);
    }

    @Test
    void testSearchRestaurantsByLocationAndCuisineTypeMultipleResults() {
        // Arrange
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Test Restaurant 1");
        restaurant1.setLocation("Pará");
        restaurant1.setCuisineType("VEGANA");
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Test Restaurant 2");
        restaurant2.setLocation("Pará");
        restaurant2.setCuisineType("VEGANA");
        restaurantRepository.save(restaurant2);

        // Act
        List<Restaurant> result = searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType("Pará", "VEGANA");

        // Assert
        assertThat(result).isNotEmpty().contains(restaurant1, restaurant2);
    }

    @Test
    void testSearchRestaurantsByLocationAndCuisineTypeNoResults() {
        // Act & Assert
        assertThatThrownBy(() -> searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType("Nonexistent", "Nonexistent"))
                .isInstanceOf(NoResultsFoundException.class)
                .hasMessage("Nenhum restaurante encontrado");
    }
}