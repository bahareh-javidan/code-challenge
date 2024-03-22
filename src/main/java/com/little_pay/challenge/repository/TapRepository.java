package com.little_pay.challenge.repository;

import com.little_pay.challenge.model.Cost;
import com.little_pay.challenge.model.Tap;

import java.util.List;

public interface TapRepository {
    List<Cost> readCosts();

    List<Tap> readTaps();

    void exportToFile(List<String> tripToStrings);
}
