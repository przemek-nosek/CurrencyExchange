package com.envelo.currencyexchange.clients;

import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.model.external.TableInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrencyExchangeClient {

    private final RestTemplate restTemplate;
    private final static String CURRENCY_EXCHANGE_RATES_TABLE = "http://api.nbp.pl/api/exchangerates/tables/c?format=json";


    public List<ExchangeRate> getAvailableCurrencies() {
        TableInfo tableInfo = restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfo.class);

        if (tableInfo == null) {

        }

        return tableInfo.getExchangeRates();
    }
}
