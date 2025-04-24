package com.example.toanyone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ToAnyoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToAnyoneApplication.class, args);
    }

}