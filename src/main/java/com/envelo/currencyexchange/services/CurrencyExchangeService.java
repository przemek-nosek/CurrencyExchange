package com.envelo.currencyexchange.services;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeService {

    private final CurrencyExchangeClient currencyExchangeClient;

    public List<ExchangeRate> exchangeRates () {
        return currencyExchangeClient.getAvailableCurrencies();
    }
}
