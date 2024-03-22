package com.little_pay.challenge.unitTest;

import com.little_pay.challenge.model.Tap;
import com.little_pay.challenge.model.TapType;
import com.little_pay.challenge.model.Trip;
import com.little_pay.challenge.model.TripStatus;
import com.little_pay.challenge.service.CostProcessingService;
import com.little_pay.challenge.service.TapProcessingService;
import com.little_pay.challenge.service.TripProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripProcessingServiceTest {

    @Mock
    private TapProcessingService tapProcessingService;

    @Mock
    private CostProcessingService costProcessingService;

    @InjectMocks
    private TripProcessingService tripProcessingService;

    @Test
    void testProcessSingleCompletedTrip() {
        Map<String, List<Tap>> groupedTaps = new HashMap<>();
        String card1 = "111111111111";

        Tap tap1 = Tap.builder().id(1).pan(card1).date(LocalDateTime.now().minusSeconds(60)).bus("bus1").company("c1").stop("stop3").status(TapType.ON).build();
        Tap tap2 = Tap.builder().id(2).pan(card1).date(LocalDateTime.now().minusSeconds(40)).bus("bus1").company("c1").stop("stop1").status(TapType.OFF).build();
        groupedTaps.put(card1, new ArrayList<>() {{
            add(tap1);
            add(tap2);
        }});

        when(tapProcessingService.getGroupedTaps()).thenReturn(groupedTaps);
        when(tapProcessingService.findMatchingTap(tap1, groupedTaps.get(card1))).thenReturn(tap2);

        List<Trip> result = tripProcessingService.processTrips();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testProcessSingleUncompletedTapOnTrip() {
        Map<String, List<Tap>> groupedTaps = new HashMap<>();
        String card1 = "111111111111";

        Tap tap1 = Tap.builder().id(1).pan(card1).date(LocalDateTime.now().minusSeconds(60)).bus("bus1").company("c1").stop("stop3").status(TapType.ON).build();
        groupedTaps.put(card1, new ArrayList<>() {{
            add(tap1);
        }});

        when(tapProcessingService.getGroupedTaps()).thenReturn(groupedTaps);
        when(tapProcessingService.findMatchingTap(tap1, groupedTaps.get(card1))).thenReturn(null);
        when(costProcessingService.evaluateMaxCharge("stop3")).thenReturn(18.0D);
        List<Trip> result = tripProcessingService.processTrips();

        assertEquals(1, result.size());
        assertEquals(TripStatus.INCOMPLETE, result.get(0).getStatus());
        assertEquals(18, result.get(0).getChargeAmount());
        assertNotNull(result.get(0).getStarted());
    }

    @Test
    void testProcessSingleUncompletedTapOffTrip() {
        Map<String, List<Tap>> groupedTaps = new HashMap<>();
        String card1 = "111111111111";

        Tap tap1 = Tap.builder().id(1).pan(card1).date(LocalDateTime.now().minusSeconds(60)).bus("bus1").company("c1").stop("stop3").status(TapType.OFF).build();
        groupedTaps.put(card1, new ArrayList<>() {{
            add(tap1);
        }});

        when(tapProcessingService.getGroupedTaps()).thenReturn(groupedTaps);
        when(tapProcessingService.findMatchingTap(tap1, groupedTaps.get(card1))).thenReturn(null);
        when(costProcessingService.evaluateMaxCharge("stop3")).thenReturn(22D);
        List<Trip> result = tripProcessingService.processTrips();

        assertEquals(1, result.size());
        assertEquals(TripStatus.INCOMPLETE, result.get(0).getStatus());
        assertEquals(22, result.get(0).getChargeAmount());
        assertNotNull(result.get(0).getFinished());
    }

    @Test
    void testProcessSingleCancelledTrip() {
        Map<String, List<Tap>> groupedTaps = new HashMap<>();
        String card1 = "111111111111";

        Tap tap1 = Tap.builder().id(1).pan(card1).date(LocalDateTime.now().minusSeconds(60)).bus("bus1").company("c1").stop("stop3").status(TapType.ON).build();
        Tap tap2 = Tap.builder().id(2).pan(card1).date(LocalDateTime.now().minusSeconds(10)).bus("bus1").company("c1").stop("stop3").status(TapType.OFF).build();
        groupedTaps.put(card1, new ArrayList<>() {{
            add(tap1);
            add(tap2);
        }});

        when(tapProcessingService.getGroupedTaps()).thenReturn(groupedTaps);
        when(tapProcessingService.findMatchingTap(tap1, groupedTaps.get(card1))).thenReturn(tap2);
        List<Trip> result = tripProcessingService.processTrips();

        assertEquals(1, result.size());
        assertEquals(TripStatus.CANCELED, result.get(0).getStatus());
    }
}