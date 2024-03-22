package com.little_pay.challenge.repository;

import com.little_pay.challenge.exception.CostsNotAvailableException;
import com.little_pay.challenge.exception.FileGenerationException;
import com.little_pay.challenge.exception.TapsNotAvailableException;
import com.little_pay.challenge.model.Cost;
import com.little_pay.challenge.model.Tap;

import java.util.List;

public interface TapRepository {
    List<Cost> readCosts() throws CostsNotAvailableException;

    List<Tap> readTaps() throws TapsNotAvailableException;

    void exportToFile(List<String> tripToStrings) throws FileGenerationException;
}
