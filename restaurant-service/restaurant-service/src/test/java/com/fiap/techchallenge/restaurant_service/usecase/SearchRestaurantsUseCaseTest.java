package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.exceptions.NoResultsFoundException;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SearchRestaurantsUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

class SearchRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private SearchRestaurantsUseCase searchRestaurantsUseCase;

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
    void testSearchRestaurantsByLocationAndCuisineTypeSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.findByLocationAndCuisineType( "location", "cuisineType"))
                .thenReturn(List.of(restaurant));

        // Act
        List<Restaurant> result = searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType("location", "cuisineType");

        // Assert
        assertThat(result).isNotEmpty()
                .contains(restaurant);
    }

    @Test
    void testSearchRestaurantsByLocationAndCuisineTypeNoResults() {
        // Arrange
        when(restaurantRepository.findByLocationAndCuisineType( "location", "cuisineType"))
                .thenReturn(Collections.emptyList());

        // Act
        Throwable throwable = catchThrowable(() -> searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType( "location", "cuisineType"));

        // Assert
        assertThat(throwable).isInstanceOf(NoResultsFoundException.class).hasMessage("Nenhum restaurante encontrado");
    }
}