package com.little_pay.challenge.repository.impl;

import com.little_pay.challenge.exception.FileGenerationException;
import com.little_pay.challenge.exception.ResourceNotFoundException;
import com.little_pay.challenge.model.Cost;
import com.little_pay.challenge.model.Tap;
import com.little_pay.challenge.model.TapType;
import com.little_pay.challenge.repository.TapRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Repository
@Log4j2
public class TapRepositoryImpl implements TapRepository {
    private static final String COMMA = ",";

    @Value("${costs.file.path}")
    private String costsFilePath;

    @Value("${taps.file.path}")
    private String tapsFilePath;

    @Value("${export.file.path}")
    private String exportFilePath;

    @Override
    public List<Cost> readCosts() {
        try {
            return new CsvToBeanBuilder<Cost>(new FileReader(costsFilePath))
                    .withType(Cost.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException(costsFilePath);
        }
    }

    @Override
    public List<Tap> readTaps() {
        Function<String, Tap> mapToItem = line -> {
            String[] recordArray = line.split(COMMA);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy H:m:s");

            return Tap.builder()
                    .id(Integer.parseInt(recordArray[0].trim()))
                    .date(LocalDateTime.parse(recordArray[1].trim(), formatter))
                    .status(TapType.valueOf(recordArray[2].trim()))
                    .stop(recordArray[3].trim())
                    .company(recordArray[4].trim())
                    .bus(recordArray[5].trim())
                    .pan(recordArray[6].trim()).build();
        };


        try {
            File file = new File(tapsFilePath);
            InputStream inputFS = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            // skip the header of the csv then ignore empty lines then map them to item
            List<Tap> inputList = br.lines().skip(1).filter(line -> !line.isEmpty()).map(mapToItem).sorted().toList();
            br.close();
            return inputList;
        } catch (IOException e) {
            throw new ResourceNotFoundException(tapsFilePath);
        }
    }

    @Override
    public void exportToFile(List<String> tripToStrings) {
        Path newFilePath = Path.of(exportFilePath);
        try {
            Files.deleteIfExists(newFilePath);
            Files.write(newFilePath, "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status\n".getBytes());
            Files.write(newFilePath, tripToStrings, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new FileGenerationException();
        }
    }
}
