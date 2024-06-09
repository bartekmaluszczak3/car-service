package org.example.carservice.handler;

import org.example.carservice.dto.CarResponse;
import org.example.carservice.entity.Car;
import org.example.carservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.example.carservice.utils.CarMapper.carResponseFromEntity;

@Service

public class QueryCarHandler {
    @Autowired
    private CarRepository carRepository;

    public CarResponse getCar(String name) throws Exception {
        Optional<Car> car = carRepository.findByName(name);
        if(car.isEmpty()){
            throw new Exception(String.format("Car for given name %s not found", name));
        }
        return carResponseFromEntity(car.get());
    }
}
