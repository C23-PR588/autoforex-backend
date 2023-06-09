package com.projects.autoforexbackend.wallet.repository;
import com.projects.autoforexbackend.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUsername(String username);
}