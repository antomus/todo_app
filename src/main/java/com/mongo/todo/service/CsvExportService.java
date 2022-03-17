package com.mongo.todo.service;

import com.mongo.todo.AllowedCurrency;
import com.mongo.todo.model.CryptoCurrency;
import com.mongo.todo.repository.CryptoCurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;

@Service
@Slf4j
public class CsvExportService {
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    public CsvExportService(CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }
    public void writeCurrenciesToCsv(Writer writer) {
        String[] HEADERS = { "Cryptocurrency Name", "Min Price", "Max Price" };

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            for (AllowedCurrency currency : AllowedCurrency.values()) {
                String code = currency.toString();

                csvPrinter.printRecord(code,
                        calculateMinByCurrency(code),
                        calculateMaxByCurrency(code));
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }

    private double calculateMinByCurrency(String code) {
        CryptoCurrency record = cryptoCurrencyRepository.findTopByCodeOrderByPriceAsc(code).get();
        if(record != null) {
            return record.getPrice();
        } else {
            return 0.0;
        }
    }

    private double calculateMaxByCurrency(String code) {
        CryptoCurrency record = cryptoCurrencyRepository.findTopByCodeOrderByPriceDesc(code).get();
        if(record != null) {
            return record.getPrice();
        } else {
            return 0.0;
        }
    }
}
