package com.fiap.techchallenge.restaurant_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.RestaurantDTO;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        List<Restaurant> tests = restaurantRepository.findByNameContaining("Test");
        if(tests.isEmpty()){
            restaurantRepository.save(getRestaurant());
        }
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

        // Act & Assert
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }

    @Test
    void testSearchRestaurantsByLocationAndCuisineSuccess() throws Exception {
        // Arrange
        String location = "123 Test St";
        String cuisineType = "Italian";

        // Act & Assert
        mockMvc.perform(get("/restaurants/search")
                        .param("location", location)
                        .param("cuisineType", cuisineType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value(location))
                .andExpect(jsonPath("$[0].cuisineType").value(cuisineType));
    }

    @Test
    void testSearchAllRestaurantsSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/restaurants/search-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testSearchRestaurantsByNameSuccess() throws Exception {
        // Arrange
        String name = "Test Restaurant";

        // Act & Assert
        mockMvc.perform(get("/restaurants/search-by-name")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));
    }

    static Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        return restaurant;
    }
}