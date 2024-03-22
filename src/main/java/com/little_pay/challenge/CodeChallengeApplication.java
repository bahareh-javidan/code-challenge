package com.little_pay.challenge;

import com.little_pay.challenge.exception.FileGenerationException;
import com.little_pay.challenge.exception.ResourceNotFoundException;
import com.little_pay.challenge.model.Trip;
import com.little_pay.challenge.service.TripProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Log4j2
public class CodeChallengeApplication implements CommandLineRunner {
    private final TripProcessingService tripProcessingService;

    public static void main(String[] args) {
        SpringApplication.run(CodeChallengeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            List<Trip> trips = tripProcessingService.processTrips();
            tripProcessingService.generateTripCsvFile(trips);
        } catch (FileGenerationException e) {
            log.error("Cannot create file to present the result. Make sure you have access or enough space to create file");
        } catch (ResourceNotFoundException e) {
            log.error("Cannot read from csv file. Please make sure that file exists in " + e.getResource());
        }
    }

}
