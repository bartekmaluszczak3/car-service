package org.example.carservice.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteCarCommand {
    String name;
}
