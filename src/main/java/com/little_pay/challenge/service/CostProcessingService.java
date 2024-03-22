package com.little_pay.challenge.service;

import com.little_pay.challenge.exception.CostsNotAvailableException;
import com.little_pay.challenge.exception.InvalidResourceRuntimeException;
import com.little_pay.challenge.model.Cost;
import com.little_pay.challenge.repository.TapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CostProcessingService {

    private final TapRepository tapRepository;

    public List<Cost> getTripCosts() {
        try {
            return tapRepository.readCosts();
        } catch (CostsNotAvailableException e) {
            log.error("Cannot read from the cost.csv file. Please make sure that there is a csv file called cost.csv in resources folder");
            throw new InvalidResourceRuntimeException();
        }
    }


    public Double evaluateCharge(String origin, String destination) {
        List<Cost> tripCosts = getTripCosts();

        Cost tripCost = findTripCost(origin, destination, tripCosts);

        return tripCost != null ? tripCost.getAmount() : null;
    }

    public Double evaluateMaxCharge(String origin) {
        List<Cost> tripCosts = getTripCosts();
        return tripCosts.stream()
                .filter(cost -> !cost.getOrigin().equals(origin))
                .mapToDouble(Cost::getAmount)
                .max()
                .orElse(0.0);

    }

    private Cost findTripCost(String origin, String destination, List<Cost> tripCosts) {
        for (Cost cost : tripCosts) {
            if ((origin.equalsIgnoreCase(cost.getOrigin()) && destination.equalsIgnoreCase(cost.getDestination())) ||
                    (origin.equalsIgnoreCase(cost.getDestination()) && destination.equalsIgnoreCase(cost.getOrigin()))) {
                return cost;
            }
        }
        return null; // No matching cost found
    }

}
