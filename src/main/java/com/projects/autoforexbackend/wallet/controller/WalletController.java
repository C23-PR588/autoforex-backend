package com.projects.autoforexbackend.wallet.controller;

import org.springframework.web.bind.annotation.*;
import com.projects.autoforexbackend.user.*;
import com.projects.autoforexbackend.wallet.model.Wallet;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final Map<String, User> users = new HashMap<>();

    public WalletController() {
        // Create a dummy user and add it to the users map
        User dummyUser = new User("john_doe");
        dummyUser.setEmail("john@example.com");
        Wallet wallet = dummyUser.getWallet();
        wallet.getCurrencies().put("IDR", 10.0);
        wallet.getCurrencies().put("USD", 11.0);
        wallet.getCurrencies().put("EUR", 3.0);
        users.put(dummyUser.getUsername(), dummyUser);
    }

    @PostMapping("/addCurrency/{username}")
    public String addCurrency(@PathVariable String username,
                              @RequestParam String currencyName,
                              @RequestParam Double currencyValue) {
        User user = users.get(username);
        if (user == null) {
            user = new User(username);
            users.put(username, user);
        }
        Wallet wallet = user.getWallet();

        if (wallet.getCurrencies().containsKey(currencyName)) {
            Double existingValue = wallet.getCurrencies().get(currencyName);
            wallet.getCurrencies().put(currencyName, existingValue + currencyValue);
        } else {
            wallet.getCurrencies().put(currencyName, currencyValue);
        }

        return "Currency added successfully.";
    }

    @GetMapping("/getCurrencies/{username}")
    public Map<String, Double> getCurrencies(@PathVariable String username) {
        User user = users.get(username);
        if (user != null) {
            return user.getWallet().getCurrencies();
        }
        return new HashMap<>();
    }
}

