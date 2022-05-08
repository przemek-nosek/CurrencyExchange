package com.envelo.currencyexchange.clients.impl;

import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.model.external.TableInfo;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.envelo.currencyexchange.enums.ErrorMessage.EXTERNAL_API_CALL_UNAVAILABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeClientImpl currencyExchangeClient;

    private final static String CURRENCY_EXCHANGE_RATES_TABLE = "http://api.nbp.pl/api/exchangerates/tables/c?format=json";

    @Test
    void getAvailableCurrencies_shouldReturnExchangeRates_whenTableInfoIsNotNullOrEmpty() {
        //given
        ExchangeRate actualExchangeRate = new ExchangeRate("currency", "code", BigDecimal.TEN, BigDecimal.ONE);
        TableInfo tableInfo = new TableInfo(
                "table",
                "no",
                LocalDate.now(),
                LocalDate.now(),
                List.of(actualExchangeRate));

        given(restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfo[].class)).willReturn(Arrays.array(tableInfo));

        //when
        List<ExchangeRate> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        //then
        then(restTemplate).should().getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfo[].class);
        assertThat(availableCurrencies).hasSize(1);

        ExchangeRate expectedExchangeRate = availableCurrencies.get(0);
        assertThat(expectedExchangeRate.getCurrency()).isEqualTo(actualExchangeRate.getCurrency());
        assertThat(expectedExchangeRate.getCode()).isEqualTo(actualExchangeRate.getCode());
        assertThat(expectedExchangeRate.getBid()).isEqualTo(actualExchangeRate.getBid());
        assertThat(expectedExchangeRate.getAsk()).isEqualTo(actualExchangeRate.getAsk());
    }

    @Test
    void getAvailableCurrencies_shouldThrowExternalApiCallException_whenTableInfoIsNull() {
        //given
        given(restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfo[].class)).willReturn(null);

        //when
        //then
        assertThatThrownBy(() -> currencyExchangeClient.getAvailableCurrencies())
                .isInstanceOf(ExternalApiCallException.class)
                .hasMessage(EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATES_TABLE));
    }

    @Test
    void getAvailableCurrencies_shouldThrowExternalApiCallException_whenTableInfoIsEmpty() {
        //given
        given(restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfo[].class)).willReturn(new TableInfo[]{});

        //when
        //then
        assertThatThrownBy(() -> currencyExchangeClient.getAvailableCurrencies())
                .isInstanceOf(ExternalApiCallException.class)
                .hasMessage(EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATES_TABLE));
    }
}