package com.envelo.currencyexchange.model.mappers;

import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeMapperTest {

    private final CurrencyExchangeMapper currencyExchangeMapper = Mappers.getMapper(CurrencyExchangeMapper.class);

    @Test
    void exchangeRateToAvailableCurrencyDto_shouldReturnAvailableCurrencyDto_whenGivenExchangeRate() {
        //given
        ExchangeRate exchangeRate = new ExchangeRate("polskie złote", "PLN", BigDecimal.TEN, BigDecimal.ONE);

        //when
        List<AvailableCurrencyDto> availableCurrencyDtos = currencyExchangeMapper.exchangeRateListToAvailableCurrencyDtoList(List.of(exchangeRate));

        //then
        assertThat(availableCurrencyDtos).hasSize(1);
        AvailableCurrencyDto availableCurrencyDto = availableCurrencyDtos.get(0);

        assertThat(availableCurrencyDto).isNotNull();
        assertThat(availableCurrencyDto.getCurrency()).isEqualTo(exchangeRate.getCurrency());
        assertThat(availableCurrencyDto.getCode()).isEqualTo(exchangeRate.getCode());
    }
}