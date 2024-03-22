package com.little_pay.challenge;

import com.little_pay.challenge.controller.TripController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CodeChallengeApplication implements CommandLineRunner {
    private final TripController tripController;

    public static void main(String[] args) {
        SpringApplication.run(CodeChallengeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        tripController.processTrips();
    }

}
