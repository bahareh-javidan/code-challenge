package com.little_pay.challenge.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.StringJoiner;

@Setter
@Getter
public class Trip {
    private LocalDateTime started;
    private LocalDateTime finished;
    private long durationInSecs;
    private String fromStopId;
    private String toStopId;
    private Double chargeAmount;
    private String companyId;
    private String busId;
    private String pan;
    private TripStatus status;

    public static String convertTripToString(Trip t) {
        return new StringJoiner(", ", "", "")
                .add(formatDate(t.getStarted()))
                .add(formatDate(t.getFinished()))
                .add(String.valueOf(t.getDurationInSecs()))
                .add(t.getFromStopId())
                .add(t.getToStopId() != null ? t.getToStopId() : "")
                .add("$" + t.getChargeAmount())
                .add(t.getCompanyId() != null ? t.getCompanyId() : "")
                .add(t.getBusId() != null ? t.getBusId() : "")
                .add(t.getPan())
                .add(t.getStatus().name())
                .toString();
    }

    private static String formatDate(LocalDateTime date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "  date unavailable  ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip trip)) return false;
        return getDurationInSecs() == trip.getDurationInSecs() &&
                Objects.equals(getStarted(), trip.getStarted()) &&
                Objects.equals(getFinished(), trip.getFinished()) &&
                Objects.equals(getFromStopId(), trip.getFromStopId()) &&
                Objects.equals(getToStopId(), trip.getToStopId()) &&
                Objects.equals(getChargeAmount(), trip.getChargeAmount()) &&
                Objects.equals(getCompanyId(), trip.getCompanyId()) &&
                Objects.equals(getBusId(), trip.getBusId()) &&
                Objects.equals(getPan(), trip.getPan()) &&
                getStatus() == trip.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStarted(), getFinished(), getDurationInSecs(), getFromStopId(), getToStopId(), getChargeAmount(), getCompanyId(), getBusId(), getPan(), getStatus());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Trip.class.getSimpleName() + "[", "]")
                .add("started=" + started)
                .add("finished=" + finished)
                .add("durationInSecs=" + durationInSecs)
                .add("fromStopId='" + fromStopId + "'")
                .add("toStopId='" + toStopId + "'")
                .add("chargeAmount=" + chargeAmount)
                .add("companyId='" + companyId + "'")
                .add("busId='" + busId + "'")
                .add("pan='" + pan + "'")
                .add("status=" + status)
                .toString();
    }
}
