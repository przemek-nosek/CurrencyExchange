package com.envelo.currencyexchange.controllers;

import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency_exchanges")
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @GetMapping
    public List<ExchangeRate> exchangeRateList() {
        return currencyExchangeService.exchangeRates();
    }
}
