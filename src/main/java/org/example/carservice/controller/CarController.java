package org.example.carservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carservice.command.CreateCarCommand;
import org.example.carservice.command.DeleteCarCommand;
import org.example.carservice.dto.CarResponse;
import org.example.carservice.handler.CommandCarHandler;
import org.example.carservice.handler.QueryCarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    @Autowired
    CommandCarHandler carHandler;

    @Autowired
    QueryCarHandler queryCarHandler;

    @PostMapping("")
    public ResponseEntity<CarResponse> createCar(@RequestBody CreateCarCommand createCarCommand){
        CarResponse carResponse = carHandler.createCar(createCarCommand);
        log.info("Received create car request with name = {}", carResponse.getName());
        return ResponseEntity.ok(carResponse);
    }

    @DeleteMapping("")
    public ResponseEntity<CarResponse> deleteCar(@RequestBody DeleteCarCommand deleteCarCommand) throws Exception {
        log.info("Received delete car request with name = {}", deleteCarCommand.getName());
        return ResponseEntity.ok(carHandler.deleteCar(deleteCarCommand));
    }

    @GetMapping("{name}")
    public ResponseEntity<CarResponse> getCarByName(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(queryCarHandler.getCar(name));
    }
}
