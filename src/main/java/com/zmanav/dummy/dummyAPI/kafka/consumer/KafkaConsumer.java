package com.zmanav.dummy.dummyAPI.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zmanav.dummy.dummyAPI.kafka.payload.RestaurantPayload;
import com.zmanav.dummy.dummyAPI.model.ResponseId;
import com.zmanav.dummy.dummyAPI.model.Restaurant;
import com.zmanav.dummy.dummyAPI.redis.service.RedisService;
import com.zmanav.dummy.dummyAPI.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private RestaurantService restaurantService;
    private RedisService redisService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public KafkaConsumer(RestaurantService restaurantService, RedisService redisService) {
        this.restaurantService = restaurantService;
        this.redisService = redisService;
    }

    @KafkaListener(topics = "request_restaurant", groupId = "testDummyGroup")
    public void consumeTest(RestaurantPayload restaurantPayload) throws JsonProcessingException {
        LOGGER.info(String.format("Message Received -> %s", restaurantPayload.toString()));

        String requestType = restaurantPayload.getRequestType();
        switch (requestType){
            case "post":
                Restaurant newRestaurant = restaurantService.addRestaurant(
                        new Restaurant(restaurantPayload.getName(), restaurantPayload.getAddress(), restaurantPayload.getEmail(), restaurantPayload.getRating())
                );
                redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(newRestaurant));
                break;
            case "get_all":
                List<Restaurant> restaurants = restaurantService.getAll();
                redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(restaurants));
                break;
            case "get":
                Restaurant restaurant = restaurantService.getRestaurantById(restaurantPayload.getId());
                if (restaurant == null){
                    ResponseId responseId = new ResponseId(restaurantPayload.getRequestId(), "Requested user not found");
                    redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(responseId));
                }
                else {
                    redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(restaurant));
                }
                break;
            case "delete":
                try {
                    restaurantService.deleteRestaurant(restaurantPayload.getId());
                } catch (EmptyResultDataAccessException e){
                    ResponseId responseId = new ResponseId(restaurantPayload.getRequestId(), "Restaurant not Found");
                    redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(responseId));
                    break;
                }
                ResponseId responseId = new ResponseId(restaurantPayload.getRequestId(), "Resource deleted successfully");
                redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(responseId));
                break;
            case "put":

                Restaurant EditRestaurant = restaurantService.editRestaurant(
                        new Restaurant(restaurantPayload.getName(), restaurantPayload.getAddress(), restaurantPayload.getEmail(), restaurantPayload.getRating()),
                        restaurantPayload.getId()
                );
                if (EditRestaurant == null){
                    ResponseId EditresponseId = new ResponseId(restaurantPayload.getRequestId(), "Requested user not found");
                    redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(EditresponseId));
                }
                else {
                    redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(EditRestaurant));
                }
                break;
            default:
                ResponseId EditresponseId = new ResponseId(restaurantPayload.getRequestId(), "Requested response type not found");
                redisService.addValue(restaurantPayload.getRequestId(), objectMapper.writeValueAsString(EditresponseId));
                break;
        }

    }
}
