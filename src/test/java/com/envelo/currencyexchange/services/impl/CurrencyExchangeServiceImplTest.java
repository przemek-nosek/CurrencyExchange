package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.CurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.entities.SystemLog;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.repositories.SystemLogRepository;
import com.envelo.currencyexchange.validators.CurrencyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Mock
    private SystemLogRepository systemLogRepository;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeServiceImpl;

    @BeforeEach
    void setUp() {
        given(systemLogRepository.save(any())).willReturn(new SystemLog());
    }

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
        willDoNothing().given(currencyValidator).validateGivenCurrencies(anyList(), anyList());

        ExchangeRateDto fromCurrency = new ExchangeRateDto("dolar amerykanski", "USD", BigDecimal.TEN);
        ExchangeRateDto toCurrency = new ExchangeRateDto("dolar australijski", "AUD", BigDecimal.ONE);
        given(currencyExchangeClient.getCurrentExchangeRateForCurrency(any())).willReturn(fromCurrency, toCurrency);

        //when
        ExchangeCurrencyFromToDto exchangeCurrencyFromToDto = currencyExchangeServiceImpl.calculateCurrencyExchangeAmount(BigDecimal.TEN, "USD", "AUD");

        //then
        assertThat(exchangeCurrencyFromToDto).isNotNull();
        assertThat(exchangeCurrencyFromToDto.getFromCurrencyCode()).isEqualTo(fromCurrency.getCode());
        assertThat(exchangeCurrencyFromToDto.getToCurrencyCode()).isEqualTo(toCurrency.getCode());
        assertThat(exchangeCurrencyFromToDto.getAmountToCalculate()).isEqualTo(BigDecimal.TEN);
        assertThat(exchangeCurrencyFromToDto.getCalculatedAmount()).isEqualTo(new BigDecimal("100.000"));
    }

    @Test
    void getCurrentRatesForCurrencies_shouldReturnCurrencyDtoList_whenApiCallWasSuccessful() {
        //given
        given(currencyExchangeClient.getAvailableCurrencies()).willReturn(List.of(
                new ExchangeRateDto("currency1", "code1", BigDecimal.TEN),
                new ExchangeRateDto("currency2", "code2", BigDecimal.ONE)
        ));
        willDoNothing().given(currencyValidator).validateGivenCurrencies(anyList(), anyList());


        //when
        List<CurrencyDto> currencyDtoList = currencyExchangeServiceImpl.getCurrentRatesForCurrencies(List.of("code1"));

        //then
        assertThat(currencyDtoList).isNotEmpty();
        then(currencyExchangeClient).should().getAvailableCurrencies();
    }
}