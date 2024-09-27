package com.fiap.techchallenge.restaurant_service.domain.usecase;

import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.exceptions.NoResultsFoundException;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchRestaurantsUseCase {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public SearchRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> searchRestaurantsByLocationAndCuisineType(String location, String cuisineType) {
        List<Restaurant> restaurantList = restaurantRepository.findByLocationAndCuisineTypeDesc(location, cuisineType);

        //se for vazio retornar exception
        if(restaurantList.isEmpty()){
            throw new NoResultsFoundException("Nenhum restaurante encontrado");
        }else {
            return restaurantList;
        }
    }

    public List<Restaurant> searchRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        //se for vazio retornar exception
        if(restaurantList.isEmpty()){
            throw new NoResultsFoundException("Nenhum restaurante encontrado");
        }else {
            return restaurantList;
        }
    }

    public List<Restaurant> searchRestaurantsByName(String name) {
        List<Restaurant> restaurantList = restaurantRepository.findByNameContaining(name);
        //se for vazio retornar exception
        if(restaurantList.isEmpty()){
            throw new NoResultsFoundException("Nenhum restaurante encontrado");
        }else {
            return restaurantList;
        }
    }
}
