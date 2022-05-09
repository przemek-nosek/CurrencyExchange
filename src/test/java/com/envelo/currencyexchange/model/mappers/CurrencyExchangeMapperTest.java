package com.envelo.currencyexchange.model.mappers;

import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyExchangeMapperTest {

    private final CurrencyExchangeMapper currencyExchangeMapper = Mappers.getMapper(CurrencyExchangeMapper.class);

    @Test
    void exchangeRateToAvailableCurrencyDto_shouldReturnAvailableCurrencyDto_whenGivenExchangeRate() {
        //given
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto("polskie złote", "PLN", BigDecimal.TEN);

        //when
        List<AvailableCurrencyDto> availableCurrencyDtos = currencyExchangeMapper.exchangeRateListToAvailableCurrencyDtoList(List.of(exchangeRateDto));

        //then
        assertThat(availableCurrencyDtos).hasSize(1);
        AvailableCurrencyDto availableCurrencyDto = availableCurrencyDtos.get(0);

        assertThat(availableCurrencyDto).isNotNull();
        assertThat(availableCurrencyDto.getCurrency()).isEqualTo(exchangeRateDto.getCurrency());
        assertThat(availableCurrencyDto.getCode()).isEqualTo(exchangeRateDto.getCode());
    }
}