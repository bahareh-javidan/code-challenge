package com.little_pay.challenge.model;

import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
public class Tap implements Comparable<Tap> {
    private int id;

    @CsvDate(value = "d-M-yyyy-H:m:s")
    private LocalDateTime date;
    private TapType status;
    private String stop;
    private String company;
    private String bus;
    private String pan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tap)) return false;
        Tap tapItem = (Tap) o;
        return getId() == tapItem.getId() &&
                getDate().equals(tapItem.getDate()) &&
                getStatus() == tapItem.getStatus() &&
                getStop().equals(tapItem.getStop()) &&
                getCompany().equals(tapItem.getCompany()) &&
                getBus().equals(tapItem.getBus()) &&
                getPan().equals(tapItem.getPan());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getStatus(), getStop(), getCompany(), getBus(), getPan());
    }

    @Override
    public int compareTo(Tap o) {
        return this.getDate().compareTo(o.getDate());
    }


    @Override
    public String toString() {
        return "Tap{" +
                "id=" + id +
                ", dateTime='" + date + '\'' +
                ", status='" + status + '\'' +
                ", stop='" + stop + '\'' +
                ", company='" + company + '\'' +
                ", bus='" + bus + '\'' +
                ", cardNumber='" + pan + '\'' +
                '}';
    }
}
