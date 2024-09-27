package com.fiap.techchallenge.restaurant_service.repository;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class RestaurantRepositoryIntegrationTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    //deleteByNameContaining
    @BeforeEach
    public void setUp() {
        restaurantRepository.deleteByNameContaining("Test");
    }


    @Test
     void testShouldSaveRestaurant() {
        // Arrange
        Restaurant restaurant = getRestaurant();

        // Act
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Assert
        assertThat(savedRestaurant).isNotNull();
        assertThat(savedRestaurant.getName()).isEqualTo("Test Restaurant");
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
        restaurant = restaurantRepository.save(restaurant);

        // Act
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurant.getId());

        // Assert
        assertThat(foundRestaurant).isPresent();
        assertThat(foundRestaurant.get().getName()).isEqualTo("Test Restaurant");
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
        restaurant = restaurantRepository.save(restaurant);

        // Act
        restaurant.setLocation("456 Updated St");
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        // Assert
        assertThat(updatedRestaurant).isNotNull();
        assertThat(updatedRestaurant.getLocation()).isEqualTo("456 Updated St");
    }

    @Test
    void testShouldListAllRestaurants() {
        // Arrange
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Test Restaurant 1");
        restaurant1.setLocation("123 Test St");
        restaurant1.setCuisineType("Italian");
        restaurant1.setOpeningHours("10:00-22:00");
        restaurant1.setCapacity(100);
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Test Restaurant 2");
        restaurant2.setLocation("456 Test St");
        restaurant2.setCuisineType("Chinese");
        restaurant2.setOpeningHours("11:00-23:00");
        restaurant2.setCapacity(150);
        restaurantRepository.save(restaurant2);

        // Act
        List<Restaurant> foundRestaurants = restaurantRepository.findAll();

        // Assert
        assertThat(foundRestaurants).hasSize(2);
        assertThat(foundRestaurants).containsExactlyInAnyOrder(restaurant1, restaurant2);
    }

    @Test
    void testShouldDeleteRestaurantById() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        restaurant = restaurantRepository.save(restaurant);

        // Act
        restaurantRepository.deleteById(restaurant.getId());

        // Assert
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurant.getId());
        assertThat(foundRestaurant).isEmpty();
    }

    Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        return restaurant;
    }
}