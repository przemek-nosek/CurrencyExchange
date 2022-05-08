package com.envelo.currencyexchange.model.mappers;

import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyExchangeMapper {

    AvailableCurrencyDto exchangeRateToAvailableCurrencyDto(ExchangeRate exchangeRate);
}
