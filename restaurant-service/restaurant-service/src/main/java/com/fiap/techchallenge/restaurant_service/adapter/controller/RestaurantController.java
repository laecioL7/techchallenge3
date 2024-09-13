package com.fiap.techchallenge.restaurant_service.adapter.controller;

import com.fiap.techchallenge.restaurant_service.adapter.dto.RestaurantDTO;
import com.fiap.techchallenge.restaurant_service.adapter.mapper.RestaurantMapper;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.usecase.RegisterRestaurantUseCase;
import com.fiap.techchallenge.restaurant_service.domain.usecase.SearchRestaurantsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RegisterRestaurantUseCase registerRestaurantUseCase;
    private final SearchRestaurantsUseCase searchRestaurantsUseCase;

    private final RestaurantMapper restaurantMapper;

    public RestaurantController(RegisterRestaurantUseCase registerRestaurantUseCase,
                                SearchRestaurantsUseCase searchRestaurantsUseCase, RestaurantMapper restaurantMapper) {
        this.registerRestaurantUseCase = registerRestaurantUseCase;
        this.searchRestaurantsUseCase = searchRestaurantsUseCase;
        this.restaurantMapper = restaurantMapper;
    }

    @PostMapping
    public ResponseEntity<Restaurant> registerRestaurant(@RequestBody @Valid RestaurantDTO restaurantDTO) {
        Restaurant entity = restaurantMapper.toEntity(restaurantDTO);
        return ResponseEntity.ok(registerRestaurantUseCase.registerRestaurant(entity));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam(required = false) String location,
                                                              @RequestParam(required = false) String cuisineType) {
        List<Restaurant> restaurants = null;

        if(location != null && cuisineType != null){
            restaurants = searchRestaurantsUseCase.searchRestaurantsByLocationAndCuisineType(location, cuisineType);
        }else{
            restaurants = searchRestaurantsUseCase.searchRestaurants();
        }

        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<Restaurant>> searchRestaurantsByName(@RequestParam(required = false) String name) {
        List<Restaurant> restaurants = searchRestaurantsUseCase.searchRestaurantsByName(name);
        return ResponseEntity.ok(restaurants);
    }
}
