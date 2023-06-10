package com.projects.autoforexbackend.wallet.controller;

import com.projects.autoforexbackend.currency.model.Currency;
import com.projects.autoforexbackend.currency.model.CurrencyData;
import com.projects.autoforexbackend.userapp.model.UserApp;
import com.projects.autoforexbackend.userapp.repository.UserAppRepository;
import com.projects.autoforexbackend.wallet.repository.WalletRepository;
import org.springframework.http.HttpStatus;
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
            @RequestParam Double currencyValue,
            @RequestParam Double target
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
                Wallet newWallet = new Wallet(username, currencyName, currencyValue, target);
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
            @RequestParam Double currencyValue,
            @RequestParam Double target
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
                Wallet newWallet = new Wallet(username, currencyName, currencyValue, target);
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

    @PostMapping("/sellCurrency")
    public ResponseEntity<String> sellCurrency(
            @RequestParam String username,
            @RequestParam String currencyName,
            @RequestParam Double currencyValue
    ) {
        Optional<UserApp> optionalUser = userAppRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            List<Wallet> wallets = walletRepository.findByUsername(username);

            // Check if the wallet with the given currency name exists
            Optional<Wallet> existingWallet = wallets.stream()
                    .filter(wallet -> wallet.getCurrencyName().equalsIgnoreCase(currencyName))
                    .findFirst();

            if (existingWallet.isPresent()) {
                Wallet wallet = existingWallet.get();
                Double currentValue = wallet.getCurrencyValue();

                // Check if the wallet has sufficient currency value to sell
                if (currentValue >= currencyValue) {
                    // Get the target currency from CurrencyData
                    List<Currency> currencies = CurrencyData.getCurrencies();
                    Optional<Currency> targetCurrencyOptional = currencies.stream()
                            .filter(currency -> currency.getName().equalsIgnoreCase(currencyName))
                            .findFirst();

                    if (targetCurrencyOptional.isPresent()) {
                        Currency targetCurrency = targetCurrencyOptional.get();
                        Double targetCurrencyValue = targetCurrency.getCurrentValue();

                        // Calculate the IDR price for the currency to be sold
                        Double idrPrice = targetCurrencyValue * currencyValue;

                        // Update the IDR wallet value for the user
                        Wallet idrWallet = wallets.stream()
                                .filter(walletItem -> walletItem.getCurrencyName().equalsIgnoreCase("IDR"))
                                .findFirst()
                                .orElse(null);

                        if (idrWallet == null) {
                            idrWallet = new Wallet(username, "IDR", idrPrice);
                        } else {
                            idrWallet.setCurrencyValue(idrWallet.getCurrencyValue() + idrPrice);
                        }

                        // Deduct the sold currency value from the wallet
                        wallet.setCurrencyValue(currentValue - currencyValue);

                        // If the remaining value in the wallet is zero, remove the wallet
                        if (wallet.getCurrencyValue() == 0) {
                            walletRepository.delete(wallet);
                        } else {
                            walletRepository.save(wallet);
                        }

                        // Save the updated IDR wallet value
                        walletRepository.save(idrWallet);

                        return ResponseEntity.ok("Currency sold successfully.");
                    } else {
                        return ResponseEntity.badRequest().body("Invalid currency.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Insufficient currency value in the wallet.");
                }
            } else {
                return ResponseEntity.badRequest().body("Wallet with the given currency not found.");
            }
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    @GetMapping("/checkTargets")
    public ResponseEntity<String> checkWalletTargets() {
        List<UserApp> users = userAppRepository.findAll();

        for (UserApp user : users) {
            List<Wallet> wallets = walletRepository.findByUsername(user.getEmail());

            for (Wallet wallet : wallets) {
                if (!wallet.getCurrencyName().equalsIgnoreCase("IDR")) {
                    List<Currency> currencies = CurrencyData.getCurrencies();
                    Currency targetCurrency = null;

                    for (Currency currency : currencies) {
                        if (currency.getName().equalsIgnoreCase(wallet.getCurrencyName())) {
                            targetCurrency = currency;
                            break;
                        }
                    }
                    Double currentValue = targetCurrency.getCurrentValue();
                    Double target = wallet.getTarget();

                    if (currentValue >= target) {
                        ResponseEntity<String> sellResponse = sellCurrency(user.getEmail(), wallet.getCurrencyName(), wallet.getCurrencyValue());

                        // Check the response entity to handle any errors or success messages
                        if (sellResponse.getStatusCode() != HttpStatus.OK) {
                            // Handle the error case
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while selling currency.");
                        }
                    }
                }
            }
        }

        return ResponseEntity.ok("Wallet targets checked successfully.");
    }
}

