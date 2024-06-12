package org.example.carservice.kafka;

import org.example.carservice.command.CreateCarCommand;
import org.example.carservice.command.DeleteCarCommand;
import org.example.events.events.Event;
import org.example.events.events.eventdata.CreateCarEvent;
import org.example.events.events.eventdata.DeleteCarEvent;


import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class EventCreator {

    public static Event createCarEvent(CreateCarCommand createCarCommand){
        CreateCarEvent createCarEvent = CreateCarEvent.builder()
                .mileage(createCarCommand.getMileage())
                .name(createCarCommand.getName())
                .build();
        return Event.builder()
                .eventType("CreateCarEvent")
                .id(UUID.randomUUID().toString())
                .timestamp(Date.from(Instant.now()))
                .eventData(createCarEvent)
                .build();
    }

    public static Event deleteCarEvent(DeleteCarCommand deleteCarCommand){
        DeleteCarEvent deleteCarEvent = DeleteCarEvent.builder()
                .name(deleteCarCommand.getName())
                .build();
        return Event.builder()
                .eventType("DeleteCarEvent")
                .id(UUID.randomUUID().toString())
                .timestamp(Date.from(Instant.now()))
                .eventData(deleteCarEvent)
                .build();
    }
}
