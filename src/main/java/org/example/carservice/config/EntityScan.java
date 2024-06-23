package org.example.carservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
@org.springframework.boot.autoconfigure.domain.EntityScan({"org.example.authservice.entity", "org.example.carservice.entity"})
public class EntityScan {
}
