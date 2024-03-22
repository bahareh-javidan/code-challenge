package com.little_pay.challenge.unitTest;

import com.little_pay.challenge.exception.ResourceNotFoundException;
import com.little_pay.challenge.model.Cost;
import com.little_pay.challenge.repository.TapRepository;
import com.little_pay.challenge.service.CostProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostProcessingServiceTest {

    @Mock
    private TapRepository tapRepository;

    @InjectMocks
    private CostProcessingService costProcessingService;
    private List<Cost> costs;

    @BeforeEach
    void initialize() {
        costs = new ArrayList<>() {{
            add(Cost.builder().origin("STOP1").destination("STOP2").amount(3.2).build());
            add(Cost.builder().origin("STOP1").destination("STOP3").amount(7.2).build());
            add(Cost.builder().origin("STOP2").destination("STOP3").amount(5.2).build());
        }};
    }

    @Test
    void testGetTripCostWhenCostIsNotEmpty() {
        when(tapRepository.readCosts()).thenReturn(costs);
        List<Cost> result = costProcessingService.getTripCosts();
        assertEquals(costs.size(), result.size());

    }

    @Test
    void testThrowExceptionWhenCostIsNotAvailable() {
        when(tapRepository.readCosts()).thenThrow(new ResourceNotFoundException("Cost.csv"));
        assertThrows(ResourceNotFoundException.class, () ->
                costProcessingService.getTripCosts()
        );
    }

    @Test
    void testEvaluateChargeForSameStops() {
        Double amount = costProcessingService.evaluateCharge("STOP1", "STOP1");
        assertEquals(0D, amount);
    }

    @Test
    void testEvaluateChargeForDifferentStops() {
        when(tapRepository.readCosts()).thenReturn(costs);
        Double amount = costProcessingService.evaluateCharge("STOP1", "STOP2");
        assertEquals(3.2D, amount);

        Double amount2 = costProcessingService.evaluateCharge("STOP2", "STOP1");
        assertEquals(3.2D, amount2);
    }

    @Test
    void testEvaluateMaxChargeForOriginStop() {
        when(tapRepository.readCosts()).thenReturn(costs);
        Double amount = costProcessingService.evaluateMaxCharge("STOP1");
        assertEquals(7.2D, amount);
    }

    @Test
    void testEvaluateMaxChargeForDestinationStop() {
        when(tapRepository.readCosts()).thenReturn(costs);
        Double amount = costProcessingService.evaluateMaxCharge("STOP3");
        assertEquals(7.2D, amount);
    }
}
