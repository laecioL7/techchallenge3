package com.fiap.techchallenge.restaurant_service.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import com.fiap.techchallenge.restaurant_service.domain.usecase.RegisterRestaurantUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

class RegisterRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RegisterRestaurantUseCase registerRestaurantUseCase;

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
    void testRegisterRestaurantSuccess() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        // Act
        Restaurant result = registerRestaurantUseCase.registerRestaurant(restaurant);

        // Assert
        assertThat(result).isEqualTo(restaurant);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testRegisterRestaurantFailure() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Erro ao salvar restaurante"));

        // Act
        Throwable throwable = catchThrowable(() -> registerRestaurantUseCase.registerRestaurant(restaurant));

        // Assert
        assertThat(throwable).isInstanceOf(RuntimeException.class).hasMessage("Erro ao salvar restaurante");
        verify(restaurantRepository).save(restaurant);
    }
}