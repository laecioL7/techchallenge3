package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.controller.RestaurantController;
import com.fiap.techchallenge.restaurant_service.adapter.dto.RestaurantDTO;
import com.fiap.techchallenge.restaurant_service.adapter.mapper.RestaurantMapper;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.usecase.RegisterRestaurantUseCase;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SearchRestaurantsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegisterRestaurantUseCase registerRestaurantUseCase;

    @Mock
    private SearchRestaurantsUseCase searchRestaurantsUseCase;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantController restaurantController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    void testRegisterRestaurantSuccess() throws Exception {
        // Arrange
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setName("Test Restaurant");
        restaurantDTO.setLocation("123 Test St");
        restaurantDTO.setCuisineType("Italian");
        restaurantDTO.setOpeningHours("10:00-22:00");
        restaurantDTO.setCapacity(100);

        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setName("Test Restaurant");

        when(restaurantMapper.toEntity(any(RestaurantDTO.class))).thenReturn(restaurant);
        when(registerRestaurantUseCase.registerRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        // Act & Assert
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isOk());

        verify(registerRestaurantUseCase, times(1))
                .registerRestaurant(any(Restaurant.class));
    }

    @Test
    void testSearchRestaurantsByLocationAndCuisineSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");

        when(searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType("123 Test St", "Italian"))
                .thenReturn(Collections.singletonList(restaurant));

        // Act & Assert
        mockMvc.perform(get("/restaurants")
                        .param("location", "123 Test St")
                        .param("cuisineType", "Italian"))
                .andExpect(status().isOk());

        verify(searchRestaurantsUseCase, times(1))
                .searchRestaurantsByLocationAndCuisineType(any(String.class), any(String.class));
    }

    @Test
    void testSearchRestaurantsSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        when(searchRestaurantsUseCase.searchRestaurants()).thenReturn(Collections.singletonList(restaurant));

        // Act & Assert
        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk());

        verify(searchRestaurantsUseCase, times(1)).searchRestaurants();
    }

    @Test
    void testSearchRestaurantsByNameSuccess() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        when(searchRestaurantsUseCase.searchRestaurantsByName("Test Restaurant"))
                .thenReturn(Collections.singletonList(restaurant));

        // Act & Assert
        mockMvc.perform(get("/restaurants/search-by-name")
                        .param("name", "Test Restaurant"))
                .andExpect(status().isOk());

        verify(searchRestaurantsUseCase, times(1))
                .searchRestaurantsByName(any(String.class));

    }
}