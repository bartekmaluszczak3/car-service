package org.example.carservice.handler;

import lombok.SneakyThrows;
import org.example.authservice.entity.User;
import org.example.carservice.command.CreateCarCommand;
import org.example.carservice.command.DeleteCarCommand;
import org.example.carservice.dto.CarResponse;
import org.example.carservice.entity.Car;
import org.example.carservice.kafka.EventCreator;
import org.example.carservice.kafka.EventProducer;
import org.example.carservice.repository.CarRepository;
import org.example.events.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.example.carservice.utils.CarMapper.carResponseFromEntity;

@Service
public class CommandCarHandler {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EventProducer eventProducer;

    @SneakyThrows
    public CarResponse createCar(CreateCarCommand createCarCommand){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        Car car = Car.builder()
                .name(createCarCommand.getName())
                .mileage(createCarCommand.getMileage())
                .user(user)
                .build();
        carRepository.save(car);
        Event event = EventCreator.createCarEvent(createCarCommand);
//        eventProducer.sendCreateCarEvent(event);
        return carResponseFromEntity(car);
    }

    public CarResponse deleteCar(DeleteCarCommand deleteCarCommand) throws Exception {
        Optional<Car> car = carRepository.findByName(deleteCarCommand.getName());
        if(car.isEmpty()){
            throw new Exception(String.format("Car for given name %s not found", deleteCarCommand.getName()));
        }
        carRepository.delete(car.get());
        Event event = EventCreator.deleteCarEvent(deleteCarCommand);
        eventProducer.sendCreateCarEvent(event);
        return carResponseFromEntity(car.get());
    }
}