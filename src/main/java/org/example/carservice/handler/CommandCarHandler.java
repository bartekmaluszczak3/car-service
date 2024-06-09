package org.example.carservice.handler;

import org.example.carservice.command.CreateCarCommand;
import org.example.carservice.command.DeleteCarCommand;
import org.example.carservice.dto.CarResponse;
import org.example.carservice.entity.Car;
import org.example.carservice.repository.CarRepository;
import org.example.events.events.CreateCarEvent;
import org.example.events.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.example.carservice.utils.CarMapper.carResponseFromEntity;

@Service
public class CommandCarHandler {

    @Autowired
    private CarRepository carRepository;

    public CarResponse createCar(CreateCarCommand createCarCommand){
        Car car = Car.builder()
                .name(createCarCommand.getName())
                .mileage(createCarCommand.getMileage())
                .build();
        carRepository.save(car);
        Event event = buildCreateCarEvent(createCarCommand);
        return carResponseFromEntity(car);
    }

    private Event buildCreateCarEvent(CreateCarCommand createCarCommand){
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

    public CarResponse deleteCar(DeleteCarCommand deleteCarCommand) throws Exception {
        Optional<Car> car = carRepository.findByName(deleteCarCommand.getName());
        if(car.isEmpty()){
            throw new Exception(String.format("Car for given name %s not found", deleteCarCommand.getName()));
        }
        carRepository.delete(car.get());
        return carResponseFromEntity(car.get());
    }
}