package com.zmanav.dummy.dummyAPI.service;

import com.zmanav.dummy.dummyAPI.model.Restaurant;
import com.zmanav.dummy.dummyAPI.repository.RestaurantRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        super();
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant addRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    @Cacheable("getRestaurantById")
    public Restaurant getRestaurantById(long id){
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if (restaurant.isPresent()){
            return restaurant.get();
        }

        return null;
    }

    public Restaurant editRestaurant(Restaurant restaurant, long id) {
        Restaurant restaurantOld = getRestaurantById(id);
        if ( restaurantOld != null) {
            restaurantOld.setAddress(restaurant.getAddress());
            restaurantOld.setEmail(restaurant.getEmail());
            restaurantOld.setRating(restaurant.getRating());
            restaurantOld.setName(restaurant.getName());
            return restaurantRepository.save(restaurantOld);
        }
        return null;
    }

    public void deleteRestaurant(long id){

        restaurantRepository.deleteById(id);
    }
}
