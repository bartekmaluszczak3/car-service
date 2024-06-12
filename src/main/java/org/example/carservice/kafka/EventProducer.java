package org.example.carservice.kafka;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.events.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    public void sendCreateCarEvent(Event createCarEvent) throws JsonProcessingException {
        String jsonEvent = objectMapper.writeValueAsString(createCarEvent);
        kafkaTemplate.send("createCar_Topic", jsonEvent);
    }

    public void sendDeleteCarEvent(Event deleteCarEvent) throws JsonProcessingException {
        String jsonEvent = objectMapper.writeValueAsString(deleteCarEvent);
        kafkaTemplate.send("deleteCar_Topic", jsonEvent);
    }

}
