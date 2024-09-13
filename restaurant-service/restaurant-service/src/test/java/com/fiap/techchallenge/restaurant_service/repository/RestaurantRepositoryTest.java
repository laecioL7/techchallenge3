package com.fiap.techchallenge.restaurant_service.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class RestaurantRepositoryTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantRepositoryTest restaurantRepositoryTest;

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
     void testShouldSaveRestaurant() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Assert
        assertThat(savedRestaurant).isNotNull();
        assertThat(savedRestaurant.getName()).isEqualTo("Test Restaurant");

        // Verify that the save method was called at least once
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
     void testShouldFindRestaurantById() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);

        when(restaurantRepository.findById(any(String.class))).thenReturn(Optional.of(restaurant));

        // Act
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById("1");

        // Assert
        assertThat(foundRestaurant).isPresent();
        assertThat(foundRestaurant.get().getName()).isEqualTo("Test Restaurant");

        // Verify that the findById method was called at least once
        verify(restaurantRepository, times(1)).findById(any(String.class));
    }

    @Test
     void testShouldUpdateRestaurant() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        restaurant.setLocation("456 Updated St");
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        // Assert
        assertThat(updatedRestaurant).isNotNull();
        assertThat(updatedRestaurant.getLocation()).isEqualTo("456 Updated St");

        // Verify that the save method was called at least once
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
     void testShouldListAllRestaurants() {
        // Arrange
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Restaurant 1");
        restaurant1.setLocation("123 Test St");
        restaurant1.setCuisineType("Italian");
        restaurant1.setOpeningHours("10:00-22:00");
        restaurant1.setCapacity(100);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Restaurant 2");
        restaurant2.setLocation("456 Test St");
        restaurant2.setCuisineType("Chinese");
        restaurant2.setOpeningHours("11:00-23:00");
        restaurant2.setCapacity(150);

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        // Act
        List<Restaurant> foundRestaurants = restaurantRepository.findAll();

        // Assert
        assertThat(foundRestaurants).hasSize(2);
        assertThat(foundRestaurants).contains(restaurant1, restaurant2);

        // Verify that the findAll method was called at least once
        verify(restaurantRepository, times(1)).findAll();
    }
}