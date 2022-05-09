package com.envelo.currencyexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyExchangeApplication {
    //TODO: 1. dodanie pola w logach: String: logLevel
    //TODO: 2. refaktor zapisywania logow
    //TODO: 3. postawic docker compose
    //TODO: 4. napisac testy integracyjne dla serwisu
    //TODO: 5. dopisac logi do exceptionow


    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeApplication.class, args);
    }

}
