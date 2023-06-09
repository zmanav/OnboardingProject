package com.zmanav.dummy.dummyAPI.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zmanav.dummy.dummyAPI.kafka.producer.KafkaProducer;
import com.zmanav.dummy.dummyAPI.kafka.payload.RestaurantPayload;
import com.zmanav.dummy.dummyAPI.model.ResponseId;
import com.zmanav.dummy.dummyAPI.model.Restaurant;
import com.zmanav.dummy.dummyAPI.redis.service.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/async/restaurants")
public class AsyncAPI {

    private KafkaProducer kafkaProducer;
    private RedisService redisService;

    public AsyncAPI(KafkaProducer kafkaProducer, RedisService redisService) {
        this.kafkaProducer = kafkaProducer;
        this.redisService = redisService;
    }

    @PostMapping
    public ResponseEntity<ResponseId> addRestaurant(@RequestBody RestaurantPayload restaurantPayload){
        UUID id = UUID.randomUUID();
        restaurantPayload.setRequestId(id.toString());
        restaurantPayload.setRequestType("post");
        kafkaProducer.sendMessage("request_restaurant", restaurantPayload);
        ResponseId responseId = new ResponseId(id.toString());
        return new ResponseEntity<ResponseId>(responseId,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseId> getRestaurants(){
        UUID id = UUID.randomUUID();
        RestaurantPayload restaurantPayload = new RestaurantPayload();
        restaurantPayload.setRequestId(id.toString());
        restaurantPayload.setRequestType("get_all");
        kafkaProducer.sendMessage("request_restaurant", restaurantPayload);
        ResponseId responseId = new ResponseId(id.toString());
        return new ResponseEntity<ResponseId>(responseId, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseId> getRestaurant(@PathVariable("id") long Id){
        UUID id = UUID.randomUUID();
        RestaurantPayload restaurantPayload = new RestaurantPayload();
        restaurantPayload.setRequestId(id.toString());
        restaurantPayload.setId(Id);
        restaurantPayload.setRequestType("get");
        kafkaProducer.sendMessage("request_restaurant", restaurantPayload);
        ResponseId responseId = new ResponseId(id.toString());
        return new ResponseEntity<ResponseId>(responseId, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseId> deleteRestaurant(@PathVariable("id") long Id){
        UUID id = UUID.randomUUID();
        RestaurantPayload restaurantPayload = new RestaurantPayload();
        restaurantPayload.setRequestId(id.toString());
        restaurantPayload.setId(Id);
        restaurantPayload.setRequestType("delete");
        kafkaProducer.sendMessage("request_restaurant", restaurantPayload);
        ResponseId responseId = new ResponseId(id.toString());
        return new ResponseEntity<ResponseId>(responseId, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseId> editRestaurant(@PathVariable("id") long Id, @RequestBody Restaurant restaurant){
        UUID id = UUID.randomUUID();
        RestaurantPayload restaurantPayload = new RestaurantPayload();
        restaurantPayload.setRequestId(id.toString());
        restaurantPayload.setId(Id);
        restaurantPayload.setRequestType("put");
        restaurantPayload.setName(restaurant.getName());
        restaurantPayload.setEmail(restaurant.getEmail());
        restaurantPayload.setAddress(restaurant.getAddress());
        restaurantPayload.setRating(restaurant.getId());
        kafkaProducer.sendMessage("request_restaurant", restaurantPayload);
        ResponseId responseId = new ResponseId(id.toString());
        return new ResponseEntity<ResponseId>(responseId, HttpStatus.OK);
    }

    @GetMapping("/fetch_response")
    public ResponseEntity<Object> fetchResponse(@RequestParam(name = "id") String id) throws JsonProcessingException {
        Object redisResponse = redisService.getValue(id);
        if (redisResponse == null) {
            return new ResponseEntity<Object>("Request ID not found", HttpStatus.BAD_REQUEST);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Object parsedObject = objectMapper.readTree(redisResponse.toString());
        return ResponseEntity.ok(parsedObject);
    }
}
