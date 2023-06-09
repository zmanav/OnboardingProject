package com.zmanav.dummy.dummyAPI.kafka.producer;

import com.zmanav.dummy.dummyAPI.kafka.payload.RestaurantPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate<String, RestaurantPayload> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, RestaurantPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, RestaurantPayload restaurantPayload) {
        LOGGER.info(restaurantPayload.toString());
        Message<RestaurantPayload> message = MessageBuilder.withPayload(restaurantPayload).
                setHeader(KafkaHeaders.TOPIC, topic).build();
        kafkaTemplate.send(message);
    }
}
