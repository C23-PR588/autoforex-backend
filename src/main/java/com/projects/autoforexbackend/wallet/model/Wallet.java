package com.projects.autoforexbackend.wallet.model;

import javax.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String currencyName;
    private Double currencyValue;
    private Double target;

    public Wallet(String username, String currencyName, Double currencyValue, Double target) {
        this.username = username;
        this.currencyName = currencyName;
        this.currencyValue = currencyValue;
        this.target = target;
    }

    public Wallet(String username, String currencyName, Double currencyValue) {
        this.username = username;
        this.currencyName = currencyName;
        this.currencyValue = currencyValue;
    }

    public Wallet() {

    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(Double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }
}
