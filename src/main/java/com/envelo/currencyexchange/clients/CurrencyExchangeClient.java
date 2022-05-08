package com.envelo.currencyexchange.clients;

import com.envelo.currencyexchange.model.external.ExchangeRate;

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
    List<ExchangeRate> getAvailableCurrencies();
}
