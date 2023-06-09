package com.projects.autoforexbackend.wallet.controller;

import com.projects.autoforexbackend.currency.model.Currency;
import com.projects.autoforexbackend.currency.model.CurrencyData;
import com.projects.autoforexbackend.userapp.model.UserApp;
import com.projects.autoforexbackend.userapp.repository.UserAppRepository;
import com.projects.autoforexbackend.wallet.repository.WalletRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projects.autoforexbackend.wallet.model.Wallet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final UserAppRepository userAppRepository;
    private final WalletRepository walletRepository;

    public WalletController(UserAppRepository userAppRepository, WalletRepository walletRepository) {
        this.userAppRepository = userAppRepository;
        this.walletRepository = walletRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addWallet(
            @RequestParam String username,
            @RequestParam String currencyName,
            @RequestParam Double currencyValue
    ) {
        Optional<UserApp> optionalUser = userAppRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            List<Wallet> wallets = walletRepository.findByUsername(username);

            // Check if a wallet with the same currency name already exists
            Optional<Wallet> existingWallet = wallets.stream()
                    .filter(wallet -> wallet.getCurrencyName().equals(currencyName))
                    .findFirst();

            if (existingWallet.isPresent()) {
                Wallet wallet = existingWallet.get();
                Double currentValue = wallet.getCurrencyValue();
                wallet.setCurrencyValue(currentValue + currencyValue);
                walletRepository.save(wallet);
            } else {
                Wallet newWallet = new Wallet(username, currencyName, currencyValue);
                walletRepository.save(newWallet);
            }

            return ResponseEntity.ok("Wallet added/updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    @GetMapping("/getCurrencies/{username}")
    public Map<String, Double> getCurrencies(@PathVariable String username) {
        List<Wallet> wallets = walletRepository.findByUsername(username);
        Map<String, Double> walletData = new HashMap<>();

        for (Wallet wallet : wallets) {
            walletData.put(wallet.getCurrencyName(), wallet.getCurrencyValue());
        }

        return walletData;
    }

    @PostMapping("/buyCurrency")
    public ResponseEntity<String> buyCurrency(
            @RequestParam String username,
            @RequestParam String currencyName,
            @RequestParam Double currencyValue
    ) {
        Optional<UserApp> optionalUser = userAppRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            List<Wallet> wallets = walletRepository.findByUsername(username);

            List<Currency> currencies = CurrencyData.getCurrencies();
            Currency targetCurrency = null;

            for (Currency currency : currencies) {
                if (currency.getName().equalsIgnoreCase(currencyName)) {
                    targetCurrency = currency;
                    break;
                }
            }

            if (targetCurrency == null) {
                return ResponseEntity.badRequest().body("Currency not found.");
            }

            Double targetCurrencyValue = targetCurrency.getCurrentValue();

            // Calculate the total value in IDR required to buy the target currency
            Double totalIDRValue = targetCurrencyValue * currencyValue;

            // Check if the user has sufficient funds in the IDR wallet
            Wallet idrWallet = wallets.stream()
                    .filter(wallet -> wallet.getCurrencyName().equalsIgnoreCase("IDR"))
                    .findFirst()
                    .orElse(null);
            if (idrWallet == null || idrWallet.getCurrencyValue() < totalIDRValue) {
                return ResponseEntity.badRequest().body("Insufficient funds in IDR wallet.");
            }

            // Check if a wallet with the same currency name already exists
            Optional<Wallet> existingWallet = wallets.stream()
                    .filter(wallet -> wallet.getCurrencyName().equalsIgnoreCase(currencyName))
                    .findFirst();

            if (existingWallet.isPresent()) {
                Wallet wallet = existingWallet.get();
                Double currentValue = wallet.getCurrencyValue();
                wallet.setCurrencyValue(currentValue + currencyValue);
                walletRepository.save(wallet);
            } else {
                Wallet newWallet = new Wallet(username, currencyName, currencyValue);
                walletRepository.save(newWallet);
            }

            // Update the IDR wallet by reducing the value
            idrWallet.setCurrencyValue(idrWallet.getCurrencyValue() - totalIDRValue);
            walletRepository.save(idrWallet);

            return ResponseEntity.ok("Wallet updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

}

