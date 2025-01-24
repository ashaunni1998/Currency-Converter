package com.myproject.currency_converter.controller;

import com.myproject.currency_converter.dto.ConversionRequest;
import com.myproject.currency_converter.dto.ConversionResponse;

import com.myproject.currency_converter.service.ExchangeRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    private ExchangeRateServiceImpl exchangeRateService;

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRates(@RequestParam(value = "base", defaultValue = "USD") String base) {
        Map<String, Double> rates = exchangeRateService.getExchangeRates(base);
        return new ResponseEntity<>(rates,HttpStatus.OK);
    }

    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convertCurrency(@RequestBody ConversionRequest request) {
        double convertedAmount = exchangeRateService.convertCurrency(request.getFrom(), request.getTo(), request.getAmount());
        ConversionResponse response = new ConversionResponse(
                request.getFrom(),
                request.getTo(),
                request.getAmount(),
                convertedAmount
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}