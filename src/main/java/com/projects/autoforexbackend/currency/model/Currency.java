package com.projects.autoforexbackend.currency.model;
public class Currency {
    private final String name;
    private double currentValue;

    public Currency(String name, double currentValue) {
        this.name = name;
        this.currentValue = currentValue;
    }

    public String getName() {
        return name;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
}