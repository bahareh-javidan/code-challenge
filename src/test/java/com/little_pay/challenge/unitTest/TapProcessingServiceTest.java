package com.little_pay.challenge.unitTest;

import com.little_pay.challenge.model.Tap;
import com.little_pay.challenge.model.TapType;
import com.little_pay.challenge.repository.TapRepository;
import com.little_pay.challenge.service.TapProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TapProcessingServiceTest {

    @Mock
    private TapRepository tapRepository;

    @InjectMocks
    private TapProcessingService tapProcessingService;
    private List<Tap> tapList;

    @BeforeEach
    void initialize() {
        tapList = new ArrayList<>() {{
            add(Tap.builder().id(1).pan("123123123123").date(LocalDateTime.now().minusSeconds(70)).bus("bus1").company("c1").stop("stop1").status(TapType.OFF).build());
            add(Tap.builder().id(1).pan("111111111111").date(LocalDateTime.now().minusSeconds(40)).bus("bus1").company("c1").stop("stop1").status(TapType.OFF).build());
            add(Tap.builder().id(2).pan("222222222222").date(LocalDateTime.now().minusSeconds(50)).bus("bus2").company("c2").stop("stop2").status(TapType.ON).build());
            add(Tap.builder().id(3).pan("111111111111").date(LocalDateTime.now().minusSeconds(60)).bus("bus1").company("c1").stop("stop3").status(TapType.ON).build());
            add(Tap.builder().id(4).pan("222222222222").date(LocalDateTime.now().minusSeconds(35)).bus("bus2").company("c2").stop("stop3").status(TapType.OFF).build());
            add(Tap.builder().id(5).pan("333333333333").date(LocalDateTime.now().minusSeconds(30)).bus("bus1").company("c1").stop("stop1").status(TapType.ON).build());
            add(Tap.builder().id(6).pan("444444444444").date(LocalDateTime.now().minusSeconds(20)).bus("bus3").company("c1").stop("stop2").status(TapType.OFF).build());
        }};
    }

    @Test
    void testFindMatchingTapForTapOn() {
        Tap tap = Tap.builder().pan("111111111111").date(LocalDateTime.now().minusSeconds(60)).bus("bus1").stop("stop1").status(TapType.ON).build();
        Tap result = tapProcessingService.findMatchingTap(tap, tapList);
        assertNotNull(result);
        assertEquals(TapType.OFF, result.getStatus());
        assertTrue(result.getDate().isAfter(tap.getDate()));
    }

    @Test
    void testFindMatchingTapForTapOff() {
        Tap tap = Tap.builder().pan("111111111111").date(LocalDateTime.now().minusSeconds(60)).bus("bus1").stop("stop1").status(TapType.OFF).build();
        Tap result = tapProcessingService.findMatchingTap(tap, tapList);
        assertNull(result);
    }

    @Test
    void testFindMatchingTapIfTapListStartsWithTapOff() {
        Tap tap = Tap.builder().pan("123123123123").date(LocalDateTime.now().minusSeconds(70)).bus("bus1").stop("stop1").status(TapType.OFF).build();
        Tap result = tapProcessingService.findMatchingTap(tap, tapList);
        assertNull(result);
    }

    @Test
    void testGettingGroupedTaps() {
        when(tapRepository.readTaps()).thenReturn(tapList);
        Map<String, List<Tap>> result = tapProcessingService.getGroupedTaps();
        assertEquals(5, result.size());
        assertEquals(2, result.get("111111111111").size());
        assertEquals(2, result.get("222222222222").size());
        assertEquals(1, result.get("333333333333").size());
        assertEquals(1, result.get("444444444444").size());
    }



}
