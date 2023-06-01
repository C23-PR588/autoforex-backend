package com.projects.autoforexbackend.currency.model;
public class Currency {
    private String name;
    private double currentValue;

    public Currency(String name, double currentValue) {
        this.name = name;
        this.currentValue = currentValue;
    }

    // Getters and setters (or lombok annotations) go here
}