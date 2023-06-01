package com.projects.autoforexbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.projects.autoforexbackend.user.User;
import com.projects.autoforexbackend.wallet.model.Wallet;

@SpringBootApplication
public class AutoforexBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoforexBackendApplication.class, args);
	}

}
