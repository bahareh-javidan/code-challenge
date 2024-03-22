package com.little_pay.challenge.service;

import com.little_pay.challenge.model.Tap;
import com.little_pay.challenge.model.TapType;
import com.little_pay.challenge.model.Trip;
import com.little_pay.challenge.model.TripStatus;
import com.little_pay.challenge.repository.TapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TripProcessingService {
    private final TapRepository fileRepository;
    private final CostProcessingService costProcessingService;
    private final TapProcessingService tapProcessingService;

    public List<Trip> processTrips() {
        List<Trip> trips = new ArrayList<>();
        Map<String, List<Tap>> groupedTaps = tapProcessingService.getGroupedTaps();

        groupedTaps.forEach((key, tapList) -> {
            // Sort tapList by dateTime
            tapList.sort(Comparator.comparing(Tap::getDate));

            int rowIndex = 0;
            while (rowIndex < tapList.size()) {
                Tap tap = tapList.get(rowIndex);
                Tap matchingTap = tapProcessingService.findMatchingTap(tap, tapList);
                if (matchingTap != null) {
                    log.info("Tap Id: " + tap.getId() + ", Matching Tap Id: " + matchingTap.getId());

                    if (matchingTap.getStop().equals(tap.getStop())) {
                        // Cancelled Trip
                        trips.add(convertCanceledTrip(tap, matchingTap));
                    } else {
                        // Completed Trip
                        trips.add(convertCompletedTrip(tap, matchingTap));
                    }
                    // Increment the index
                    rowIndex++;
                } else {
                    // Incomplete TapType.ON trip
                    trips.add(convertIncompleteTrip(tap));
                }
                rowIndex++;
            }
        });

        return trips;
    }

    public void generateFile(List<Trip> trips) {
        generateTripCsvFile(trips);
    }

    private void generateTripCsvFile(List<Trip> trips) {
        List<String> tripToStrings = trips.stream().map(Trip::convertTripToString).toList();
        fileRepository.exportToFile(tripToStrings);
    }

    private Trip convertCompletedTrip(Tap item, Tap matchingItem) {
        Trip newTrip = new Trip();
        newTrip.setBusId(item.getBus());
        newTrip.setCompanyId(item.getCompany());
        newTrip.setPan(item.getPan());
        newTrip.setStatus(TripStatus.COMPLETED);
        newTrip.setFromStopId(item.getStop());
        newTrip.setToStopId(matchingItem.getStop());
        newTrip.setStarted(item.getDate());
        newTrip.setFinished(matchingItem.getDate());
        newTrip.setDurationInSecs(Duration.between(item.getDate(), matchingItem.getDate()).getSeconds());
        newTrip.setChargeAmount(costProcessingService.evaluateCharge(item.getStop(), matchingItem.getStop()));
        return newTrip;
    }

    private Trip convertIncompleteTrip(Tap item) {
        Trip newTrip = new Trip();
        newTrip.setBusId(item.getBus());
        newTrip.setCompanyId(item.getCompany());
        newTrip.setPan(item.getPan());
        newTrip.setStatus(TripStatus.INCOMPLETE);
        newTrip.setFromStopId(item.getStop());
        if (item.getStatus().equals(TapType.ON)) {
            newTrip.setStarted(item.getDate());
        } else {
            newTrip.setFinished(item.getDate());
        }
        newTrip.setChargeAmount(costProcessingService.evaluateMaxCharge(item.getStop()));
        return newTrip;
    }

    private Trip convertCanceledTrip(Tap item, Tap matchingItem) {
        Trip newTrip = new Trip();
        newTrip.setStarted(item.getDate());
        newTrip.setFinished(matchingItem.getDate());
        newTrip.setStatus(TripStatus.CANCELED);
        newTrip.setPan(item.getPan());
        newTrip.setFromStopId(item.getStop());
        newTrip.setToStopId(matchingItem.getStop());
        newTrip.setCompanyId(item.getCompany());
        newTrip.setBusId(item.getBus());
        newTrip.setDurationInSecs(Duration.between(item.getDate(), matchingItem.getDate()).getSeconds());
        return newTrip;
    }

}
