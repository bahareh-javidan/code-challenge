package com.little_pay.challenge.controller;

import com.little_pay.challenge.exception.FileGenerationException;
import com.little_pay.challenge.exception.ResourceNotFoundException;
import com.little_pay.challenge.service.TripProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TripController {
    private final TripProcessingService tripProcessingService;

    public void processTrips() {
        try {
            tripProcessingService.processTrips();
        } catch (FileGenerationException e) {
            log.error("Cannot create file to present the result. Make sure you have access or enough space to create file");
        } catch (ResourceNotFoundException e) {
            log.error("Cannot read from csv file. Please make sure that file exist in " + e.getResource());
        }
    }
}
