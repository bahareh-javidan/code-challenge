package com.little_pay.challenge.service;

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
        return tapRepository.readCosts();
    }

    public Double evaluateCharge(String origin, String destination) {
        List<Cost> tripCosts = getTripCosts();
        return findTripCost(origin, destination, tripCosts);
    }

    public Double evaluateMaxCharge(String origin) {
        List<Cost> tripCosts = getTripCosts();
        return tripCosts.stream()
                .filter(cost -> cost.getOrigin().equalsIgnoreCase(origin) || cost.getDestination().equalsIgnoreCase(origin))
                .mapToDouble(Cost::getAmount)
                .max()
                .orElse(0.0);

    }

    private Double findTripCost(String origin, String destination, List<Cost> tripCosts) {
        for (Cost cost : tripCosts) {
            if ((origin.equalsIgnoreCase(cost.getOrigin()) && destination.equalsIgnoreCase(cost.getDestination())) ||
                    (origin.equalsIgnoreCase(cost.getDestination()) && destination.equalsIgnoreCase(cost.getOrigin()))) {
                return cost.getAmount();
            }
        }
        return 0D;
    }
}
