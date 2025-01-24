package com.myproject.currency_converter.serviceImpl;

import java.util.Map;

public interface ExchangeRateService {

    Map<String, Double> getExchangeRates(String base);
    double convertCurrency(String from, String to, double amount);

}
