package com.projects.autoforexbackend.currency.controller;

import com.projects.autoforexbackend.currency.model.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @GetMapping("/data")
    public ResponseEntity<Map<String, Double>> getCurrenciesData() {
        String[] baseCurrencies = {"EUR", "USD", "JPY", "GBP", "SGD", "AUD", "CNY", "CAD", "MYR", "RUB"};
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=3FZ5rrqBaQzKQTLwcPtf4Zi3sSwFjE9UvSX4a9wB&currencies=IDR&base_currency=%s";

        Map<String, Double> currencyData = new HashMap<>();
        List<Currency> currencies = new ArrayList<>(); // Create a list to store Currency objects

        for (String baseCurrency : baseCurrencies) {
            String requestUrl = String.format(apiUrl, baseCurrency);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(requestUrl, Map.class);
            Map<String, Map<String, Double>> responseData = response.getBody();

            if (responseData != null) {
                Double value = responseData.get("data").get("IDR");
                currencyData.put(baseCurrency, value);
                Currency currency = new Currency(baseCurrency, value); // Create a new Currency object
                currencies.add(currency); // Add the Currency object to the list
            }
        }
        return ResponseEntity.ok(currencyData);
    }
}