package org.example.carservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class MockConsumer {
    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = "createCar_Topic")
    public void receiveCreateCar(ConsumerRecord<?, ?> consumerRecord) {
        payload = consumerRecord.value().toString();
        latch.countDown();
    }

    @KafkaListener(topics = "deleteCar_Topic")
    public void receiveDeleteCar(ConsumerRecord<?, ?> consumerRecord){
        payload = consumerRecord.value().toString();
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public String  getPayload(){
        return payload;
    }
}
