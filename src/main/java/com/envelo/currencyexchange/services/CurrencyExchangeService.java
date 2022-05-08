package com.envelo.currencyexchange.services;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;

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
}
