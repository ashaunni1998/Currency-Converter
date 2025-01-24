package com.myproject.currency_converter.service;


import com.myproject.currency_converter.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Value("${api.url}")
    private String API_URL;
    @Value("${api.key}")
    private String ACCESS_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Double> getExchangeRates(String baseCurrency) {
        try {
            String url = String.format("%s?access_key=%s", API_URL, ACCESS_KEY);
            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response.getRates() == null) {
                return Collections.emptyMap();
            }

            Map<String, Double> originalRates = response.getRates();
            Double baseRate = originalRates.get(baseCurrency);

            if (baseRate == null) {
                throw new IllegalArgumentException("Invalid base currency: " + baseCurrency);
            }

            // Convert rates with respect to the base currency
            Map<String, Double> convertedRates = new HashMap<>();
            for (Map.Entry<String, Double> entry : originalRates.entrySet()) {
                convertedRates.put(entry.getKey(), entry.getValue() / baseRate);
            }

            // Ensure base currency is 1.0
            convertedRates.put(baseCurrency, 1.0);

            return convertedRates;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }
    }

    public double convertCurrency(String from, String to, double amount) {
        Map<String, Double> rates = getExchangeRates(from);
        Double fromRate = rates.get(from);
        Double toRate = rates.get(to);
        return amount * (toRate / fromRate);
    }


}