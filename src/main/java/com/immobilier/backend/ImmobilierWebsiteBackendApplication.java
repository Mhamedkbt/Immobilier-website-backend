package com.immobilier.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling; // 1. Added import

@SpringBootApplication
@EnableAsync
@EnableScheduling // 2. Added annotation to enable the timer
public class ImmobilierWebsiteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImmobilierWebsiteBackendApplication.class, args);
    }

}