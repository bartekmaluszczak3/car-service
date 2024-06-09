package org.example.carservice.utils;

import org.example.carservice.dto.CarResponse;
import org.example.carservice.entity.Car;

public class CarMapper {

    public static CarResponse carResponseFromEntity(Car car) {
        return CarResponse.builder()
                .mileage(car.getMileage())
                .name(car.getName())
                .build();
    }
}
