package com.projects.autoforexbackend.currency.controller;

import com.projects.autoforexbackend.currency.model.Currency;
import com.projects.autoforexbackend.currency.model.CurrencyData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

        for (String baseCurrency : baseCurrencies) {
            String requestUrl = String.format(apiUrl, baseCurrency);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(requestUrl, Map.class);
            Map<String, Map<String, Double>> responseData = response.getBody();

            if (responseData != null) {
                Double value = responseData.get("data").get("IDR");
                currencyData.put(baseCurrency, value);

                List<Currency> currencies = CurrencyData.getCurrencies();
                for (Currency currency : currencies) {
                    if (currency.getName().equals(baseCurrency)) {
                        currency.setCurrentValue(value); // Update the currency value
                        break;
                    }
                }
            }
        }

        return ResponseEntity.ok(currencyData);
    }

    @PutMapping("/{currencyName}/changeValue")
    public ResponseEntity<String> changeCurrencyValue(
            @PathVariable String currencyName,
            @RequestParam double inputValue
    ) {
        List<Currency> currencies = CurrencyData.getCurrencies();

        for (Currency currency : currencies) {
            if (currency.getName().equalsIgnoreCase(currencyName)) {
                currency.setCurrentValue(inputValue);
                return ResponseEntity.ok("Currency value changed successfully.");
            }
        }

        return ResponseEntity.notFound().build();
    }
}