package org.example.carservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.example.carservice.Application;
import org.example.carservice.PostgresContainer;
import org.example.events.events.eventdata.CreateCarEvent;
import org.example.events.events.Event;
import org.example.events.events.eventdata.DeleteCarEvent;
import org.example.events.events.eventdata.EventData;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParser;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = PostgresContainer.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class SendingEventTest {

    @Autowired
    EventProducer eventProducer;

    @Autowired
    MockConsumer mockConsumer;

    @AfterEach
    void afterEach(){
        mockConsumer.resetLatch();
    }

    @SneakyThrows
    @Test
    void shouldSendCreateCarEvent() {
        // given
        var carEvent = createCarEvent();

        // when
        eventProducer.sendCreateCarEvent(carEvent);

        // then
        boolean messageConsumed = mockConsumer.getLatch().await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(messageConsumed);

        // and
        JSONObject parsedEvent = new JSONObject(mockConsumer.getPayload());
        Assertions.assertEquals("createCar", parsedEvent.get("eventType"));
        JSONObject eventData = parsedEvent.getJSONObject("eventData");
        Assertions.assertEquals(Double.parseDouble(eventData.get("mileage").toString()), 10000);
        Assertions.assertEquals(eventData.get("name"), "test");

    }

    @SneakyThrows
    @Test
    void shouldSendDeleteCarEvent(){
        // given
        var deleteEvent = deleteCarEvent();

        // when
        eventProducer.sendCreateCarEvent(deleteEvent);

        // then
        boolean messageConsumed = mockConsumer.getLatch().await(10, TimeUnit.SECONDS);

        // and
        Assertions.assertTrue(messageConsumed);
        JSONObject parsedEvent = new JSONObject(mockConsumer.getPayload());
        Assertions.assertEquals("deleteCar", parsedEvent.get("eventType"));
        JSONObject eventData = parsedEvent.getJSONObject("eventData");
        Assertions.assertEquals(eventData.get("name"), "test");
    }

    private Event createCarEvent(){
        CreateCarEvent createCarEvent = CreateCarEvent.builder()
                .mileage(10000)
                .name("test")
                .build();
        return Event.builder()
                .id(UUID.randomUUID().toString())
                .timestamp(Date.from(Instant.now()))
                .eventData(createCarEvent)
                .eventType("createCar")
                .build();
    }

    private Event deleteCarEvent(){
        DeleteCarEvent deleteCarEvent = DeleteCarEvent.builder()
                .name("test")
                .build();
        return Event.builder()
                .id(UUID.randomUUID().toString())
                .timestamp(Date.from(Instant.now()))
                .eventData(deleteCarEvent)
                .eventType("deleteCar")
                .build();
    }

}