package com.projects.autoforexbackend.wallet.model;

import java.util.HashMap;
import java.util.Map;

public class Wallet {
    private Map<String, Double> currencies = new HashMap<>();

    public Map<String, Double> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, Double> currencies) {
        this.currencies = currencies;
    }
}
