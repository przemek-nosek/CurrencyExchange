package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.validators.CurrencyValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceImplTest {

    @Mock
    private CurrencyExchangeClient currencyExchangeClient;

    @Mock
    private CurrencyExchangeMapper currencyExchangeMapper;

    @Mock
    private CurrencyValidator currencyValidator;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeServiceImpl;

    @Test
    void getAvailableCurrencies_shouldReturnAvailableCurrencyList_whenApiCallWasSuccessful() {
        //given
        given(currencyExchangeClient.getAvailableCurrencies()).willReturn(new ArrayList<>());
        given(currencyExchangeMapper.exchangeRateListToAvailableCurrencyDtoList(anyList())).willReturn(new ArrayList<>());

        //when
        currencyExchangeServiceImpl.getAvailableCurrencies();

        //then
        then(currencyExchangeClient).should().getAvailableCurrencies();
        then(currencyExchangeMapper).should().exchangeRateListToAvailableCurrencyDtoList(anyList());
    }

    @Test
    void calculateCurrencyExchangeAmount_shouldCalculateExchangeAmount_whenCurrenciesAreValid() {
        //given
        given(currencyExchangeClient.getAvailableCurrencies()).willReturn(new ArrayList<>());
        willDoNothing().given(currencyValidator).validateGivenCurrencies(anyList(), anyString(), anyString());

        ExchangeRate fromCurrency = new ExchangeRate("dolar amerykanski", "USD", BigDecimal.TEN);
        ExchangeRate toCurrency = new ExchangeRate("dolar australijski", "AUD", BigDecimal.ONE);
        given(currencyExchangeClient.getCurrentExchangeRateForCurrency(any())).willReturn(fromCurrency, toCurrency);

        //when
        ExchangeCurrencyFromToDto exchangeCurrencyFromToDto = currencyExchangeServiceImpl.calculateCurrencyExchangeAmount(BigDecimal.TEN, "USD", "AUD");

        //then
        assertThat(exchangeCurrencyFromToDto).isNotNull();
        assertThat(exchangeCurrencyFromToDto.getFromCurrencyCode()).isEqualTo(fromCurrency.getCode());
        assertThat(exchangeCurrencyFromToDto.getToCurrencyCode()).isEqualTo(toCurrency.getCode());
        assertThat(exchangeCurrencyFromToDto.getAmountToCalculate()).isEqualTo(BigDecimal.TEN);
        assertThat(exchangeCurrencyFromToDto.getCalculatedAmount()).isEqualTo(new BigDecimal("100.00"));
    }
}