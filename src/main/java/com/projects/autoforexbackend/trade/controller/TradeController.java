package com.projects.autoforexbackend.trade.controller;

import com.projects.autoforexbackend.currency.model.Currency;
import com.projects.autoforexbackend.currency.model.CurrencyData;
import com.projects.autoforexbackend.user.User;
import com.projects.autoforexbackend.wallet.controller.WalletController;
import com.projects.autoforexbackend.wallet.model.Wallet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trade")
public class TradeController {

    private final WalletController walletController;

    public TradeController(WalletController walletController) {
        this.walletController = walletController;
    }

    @PostMapping("/buyCurrency")
    public String buyCurrencyFromIDR(@RequestParam String username,
                              @RequestParam String currencyName,
                              @RequestParam Double value) {
        List<Currency> currencies = CurrencyData.getCurrencies();
        Currency targetCurrency = null;

        for (Currency currency : currencies) {
            if (currency.getName().equalsIgnoreCase(currencyName)) {
                targetCurrency = currency;
                break;
            }
        }

        if (targetCurrency == null) {
            return "Currency not found.";
        }

        Double totalPrice = targetCurrency.getCurrentValue() * value;

        User user = walletController.getUser(username);
        if (user == null) {
            user = new User(username);
            walletController.addUser(user);
        }

        Wallet wallet = user.getWallet();
        Double idrValue = wallet.getCurrencies().getOrDefault("IDR", 0.0);

        if (idrValue >= totalPrice) {
            wallet.getCurrencies().put("IDR", idrValue - totalPrice);
            walletController.addCurrency(username, currencyName, value);
            return "Currency bought successfully.";
        } else {
            return "Insufficient IDR balance.";
        }
    }

    @PostMapping("/sellCurrency/{username}")
    public String sellCurrency(@PathVariable String username,
                               @RequestParam String currencyName,
                               @RequestParam Double currencyValue) {
        User user = walletController.getUser(username);
        if (user == null) {
            return "User not found.";
        }
        Wallet wallet = user.getWallet();

        if (!wallet.getCurrencies().containsKey(currencyName)) {
            return "Currency not found in the wallet.";
        }

        Double availableValue = wallet.getCurrencies().get(currencyName);
        if (currencyValue > availableValue) {
            return "Insufficient currency value in the wallet.";
        }

        List<Currency> currencies = CurrencyData.getCurrencies();
        Currency targetCurrency = null;

        for (Currency currency : currencies) {
            if (currency.getName().equalsIgnoreCase(currencyName)) {
                targetCurrency = currency;
                break;
            }
        }

        if (targetCurrency == null) {
            return "Currency not found.";
        }

        // Calculate IDR value to add to the wallet
        Double currentValue = targetCurrency.getCurrentValue();
        Double idrValue = currencyValue * currentValue;

        // Decrease the currency value in the wallet
        Double updatedValue = availableValue - currencyValue;
        if (updatedValue <= 0) {
            wallet.getCurrencies().remove(currencyName);
        } else {
            wallet.getCurrencies().put(currencyName, updatedValue);
        }

        // Call addCurrency method to update the wallet
        walletController.addCurrency(username, "IDR", idrValue);

        return "Currency sold successfully.";
    }
}