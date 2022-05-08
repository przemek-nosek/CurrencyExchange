package com.envelo.currencyexchange.clients.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.model.external.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.envelo.currencyexchange.enums.ErrorMessage.EXTERNAL_API_CALL_UNAVAILABLE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATES_TABLE;

/**
 * An actual implementation class of {@link CurrencyExchangeClient}
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeClientImpl implements CurrencyExchangeClient { // TODO: LOG CALL IN SYSTEM AND SAVE THEM TO DB.

    private final RestTemplate restTemplate;


    /**
     * <p>Returns a list of{@link ExchangeRate}</p>
     * <p>
     * Method used to connect to external API to fetch available currencies and it's exchange rates.
     * API call returns table consisting table info and it's exchange rates.
     *
     * @return List of ExchangeRate when successfully fetched data from the API.
     * @throws ExternalApiCallException when there was an error while connecting to the API>
     */
    @Override
    public List<ExchangeRate> getAvailableCurrencies() {
        TableInfo[] tableInfo = restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfo[].class);

        if (tableInfo == null || tableInfo.length == 0) {
            throw new ExternalApiCallException(EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATES_TABLE));
        }

        return tableInfo[0].getRates();
    }

    /**
     * Returns {@link ExchangeRate}
     * Method used to get current exchange rate for given currency
     * @param currency to get exchange rate for
     * @return ExchangeRate for given currency
     */
    @Override
    public ExchangeRate getCurrentExchangeRateForCurrency(String currency) {
        TableInfo tableInfo = restTemplate.getForObject(CURRENCY_EXCHANGE_RATE, TableInfo.class, currency);

        if (tableInfo == null) {
            throw new ExternalApiCallException(EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATE));
        }

        return tableInfo.getRates().get(0);
    }
}
