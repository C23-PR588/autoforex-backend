package com.projects.autoforexbackend.user;

import com.projects.autoforexbackend.wallet.model.Wallet;

public class User {
    private String username;
    private String email;
    private Wallet wallet;

    public User(String username) {
        this.username = username;
        this.wallet = new Wallet();
    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}


