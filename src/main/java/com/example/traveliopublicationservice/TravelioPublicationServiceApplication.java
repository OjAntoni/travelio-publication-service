package com.example.traveliopublicationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class TravelioPublicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelioPublicationServiceApplication.class, args);
    }

}
