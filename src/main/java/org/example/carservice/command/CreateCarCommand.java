package org.example.carservice.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCarCommand {
    private String name;
    private double mileage;
}
