package com.little_pay.challenge.service;

import com.little_pay.challenge.model.Tap;
import com.little_pay.challenge.model.TapType;
import com.little_pay.challenge.repository.TapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TapProcessingService {
    private final TapRepository tapRepository;

    public Tap findMatchingTap(Tap tap, List<Tap> tapList) {
        if (tap.getStatus().equals(TapType.OFF)) {
            return null;
        }
        // Find matching tap based on criteria
        return tapList.stream()
                .filter(matchingTap -> matchingTap.getPan().equals(tap.getPan()))
                .filter(matchingTap -> matchingTap.getDate().isAfter(tap.getDate()))
                .filter(matchingTap -> matchingTap.getBus().equalsIgnoreCase(tap.getBus()))
                .filter(matchingTap -> matchingTap.getStatus().equals(TapType.OFF))
                .findFirst()
                .orElse(null);
    }

    public Map<String, List<Tap>> getGroupedTaps() {
        List<Tap> taps = tapRepository.readTaps();
        return taps.stream().collect(Collectors.groupingBy(Tap::getPan));
    }
}
