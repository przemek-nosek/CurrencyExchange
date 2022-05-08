package com.envelo.currencyexchange.model.mappers;

import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper from mapstruct, with componentModel set to "spring" to be automatically managed by Spring Container.
 *
 * Mapper used to do various mappings between CurrencyExchange classes.
 */

@Mapper(componentModel = "spring")
public interface CurrencyExchangeMapper {

    /**
     * Method used to map object from {@link ExchangeRate} to {@link AvailableCurrencyDto}
     *
     * @param exchangeRates list of ExchangeRate as mapping source
     * @return list of AvailableCurrencyDto as mapping target
     */
    List<AvailableCurrencyDto> exchangeRateListToAvailableCurrencyDtoList(List<ExchangeRate> exchangeRates);
}
