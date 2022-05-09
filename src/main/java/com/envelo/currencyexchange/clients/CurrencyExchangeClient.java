package com.envelo.currencyexchange.clients;

import com.envelo.currencyexchange.model.dto.ExchangeRateDto;

import java.util.List;

/**
 * Interface used to retrieve data from API calls.
 */
public interface CurrencyExchangeClient {

    /**
     * Method used to get available currencies.
     *
     * @return List of ExchangeRate
     */
    List<ExchangeRateDto> getAvailableCurrencies();

    /**
     * Method used to get current exchange rate.
     *
     * @param currency to get exchange rate for
     * @return ExchangeRate class with fetched data
     */
    ExchangeRateDto getCurrentExchangeRateForCurrency(String currency);
}
