package com.projects.autoforexbackend.currency.model;

import java.util.ArrayList;
import java.util.List;

public class CurrencyData {
    private static List<Currency> currencies = new ArrayList<>();

    public static List<Currency> getCurrencies() {
        return currencies;
    }
}
