package com.projects.autoforexbackend.currency.model;

import java.util.ArrayList;
import java.util.List;

public class CurrencyData {
    private static final List<Currency> currencies = new ArrayList<>();

    static {
        currencies.add(new Currency("EUR", 0));
        currencies.add(new Currency("USD", 0));
        currencies.add(new Currency("JPY", 0));
        currencies.add(new Currency("GBP", 0));
        currencies.add(new Currency("SGD", 0));
        currencies.add(new Currency("AUD", 0));
        currencies.add(new Currency("CNY", 0));
        currencies.add(new Currency("CAD", 0));
        currencies.add(new Currency("MYR", 0));
        currencies.add(new Currency("RUB", 0));
    }

    public static List<Currency> getCurrencies() {
        return currencies;
    }
}
