package com.zmanav.dummy.dummyAPI.controller;

import com.zmanav.dummy.dummyAPI.model.Restaurant;
import com.zmanav.dummy.dummyAPI.service.RestaurantService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sync/restaurants")
public class SyncAPI {

    private RestaurantService restaurantService;

    public SyncAPI(RestaurantService restaurantService) {
        super();
        this.restaurantService = restaurantService;
    }

    @GetMapping()
    public List<Restaurant> listAll(){
        return restaurantService.getAll();
    }

    @PostMapping()
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant){
        return new ResponseEntity<Restaurant>(restaurantService.addRestaurant(restaurant), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity getRestaurantById(@PathVariable("id") long id){
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        if (restaurant != null ){
            return new ResponseEntity<Restaurant>(restaurant, HttpStatus.OK);
        }
        return new ResponseEntity<>("Restaurant not found",HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{id}")
    public ResponseEntity updateRestaurant(@PathVariable("id") long id, @RequestBody Restaurant restaurant){
        Restaurant restaurantOld = restaurantService.editRestaurant(restaurant, id);

        if (restaurantOld != null ){
            return new ResponseEntity<Restaurant>(restaurantOld, HttpStatus.OK);
        }
        return new ResponseEntity<>("Restaurant not found",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable("id") long id){
        try {
            restaurantService.deleteRestaurant(id);
        }
        catch(EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Restaurant not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Deleted the Data Successfully", HttpStatus.OK);
    }
}
