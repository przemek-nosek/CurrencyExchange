package com.envelo.currencyexchange.services;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface used to do business logic.
 */
public interface CurrencyExchangeService {

    /**
     * Method used to call {@link CurrencyExchangeClient} method to get available currencies.
     *
     * @return list of AvailableCurrencyDto
     */
    List<AvailableCurrencyDto> getAvailableCurrencies();


    /**
     * Methods used to calculate given amount from one currency to another currency
     * Returns {@link ExchangeCurrencyFromToDto}
     * @param amount given amount to calculate
     * @param from currency to calculate from
     * @param to currency to calculate to
     * @return ExchangeCurrencyFromToDto with calculated amount, given amount and currencies
     */

    ExchangeCurrencyFromToDto calculateCurrencyExchangeAmount(BigDecimal amount, String from, String to);
}
