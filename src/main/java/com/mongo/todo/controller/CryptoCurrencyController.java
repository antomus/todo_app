package com.mongo.todo.controller;

import com.mongo.todo.AllowedCurrency;
import com.mongo.todo.model.CryptoCurrency;
import com.mongo.todo.service.CryptoCurrencyService;
import com.mongo.todo.service.CsvExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cryptocurrencies")
public class CryptoCurrencyController {

    private CryptoCurrencyService cryptoCurrencyService;
    private CsvExportService csvExportService;

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleCurrencyType(RuntimeException ex) {
        return new ResponseEntity<>("Please provide on of supported currency types BTC, ETH or XRP", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String>  handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return new ResponseEntity<>(name + " parameter is missing", HttpStatus.BAD_REQUEST);
    }

    @Autowired
    public CryptoCurrencyController(CryptoCurrencyService cryptoCurrencyService, CsvExportService csvExportService) {
        this.cryptoCurrencyService  = cryptoCurrencyService;
        this.csvExportService = csvExportService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<CryptoCurrency>> getAllRecordsByCode(
            @RequestParam("name") AllowedCurrency name ,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<CryptoCurrency> list = cryptoCurrencyService.getAllRecordsByCode(name.name(), page, size);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/minprice")
    public ResponseEntity<CryptoCurrency> getMinPrice(@RequestParam("name") AllowedCurrency name ) {
        CryptoCurrency currency = cryptoCurrencyService.getMinPriceRecord(name.name()).get();
        return new ResponseEntity<CryptoCurrency>(currency, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/maxprice")
    public ResponseEntity<CryptoCurrency> getMaxPrice(@RequestParam("name") AllowedCurrency name ) {
        CryptoCurrency currency = cryptoCurrencyService.getMaxPriceRecord(name.name()).get();
        return new ResponseEntity<CryptoCurrency>(currency, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/csv")
    public void getCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"currencies.csv\"");
        csvExportService.writeCurrenciesToCsv(servletResponse.getWriter());
    }
}
