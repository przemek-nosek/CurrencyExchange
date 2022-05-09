package com.envelo.currencyexchange.clients.impl;

import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.dto.TableInfoDto;
import com.envelo.currencyexchange.model.dto.TableInfoForOneCurrencyDto;
import com.envelo.currencyexchange.services.impl.SystemLogServiceImpl;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.envelo.currencyexchange.model.enums.ErrorMessage.EXTERNAL_API_CALL_UNAVAILABLE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATES_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SystemLogServiceImpl systemLogService;

    @InjectMocks
    private CurrencyExchangeClientImpl currencyExchangeClient;

    @BeforeEach
    void setUp() {
        willDoNothing().given(systemLogService).saveLog(anyString(), anyString());
    }

    @Test
    void getAvailableCurrencies_shouldReturnExchangeRateList_whenTableInfoIsNotNullOrEmpty() {
        //given
        ExchangeRateDto actualExchangeRateDto = new ExchangeRateDto("currency", "code", BigDecimal.TEN);
        TableInfoDto tableInfoDto = new TableInfoDto(
                "table",
                "no",
                LocalDate.now(),
                LocalDate.now(),
                List.of(actualExchangeRateDto));

        given(restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfoDto[].class)).willReturn(Arrays.array(tableInfoDto));

        //when
        List<ExchangeRateDto> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        //then
        then(restTemplate).should().getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfoDto[].class);
        assertThat(availableCurrencies).hasSize(1);

        ExchangeRateDto expectedExchangeRateDto = availableCurrencies.get(0);
        assertThat(expectedExchangeRateDto.getCurrency()).isEqualTo(actualExchangeRateDto.getCurrency());
        assertThat(expectedExchangeRateDto.getCode()).isEqualTo(actualExchangeRateDto.getCode());
        assertThat(expectedExchangeRateDto.getMid()).isEqualTo(actualExchangeRateDto.getMid());
    }


    @Test
    void getCurrentExchangeRateForCurrency_shouldReturnExchange_whenTableInfoIsNotNull() {
        //given
        String code = "code";
        ExchangeRateDto actualExchangeRateDto = new ExchangeRateDto("currency", code, BigDecimal.TEN);
        TableInfoForOneCurrencyDto tableInfoForOneCurrencyDto = new TableInfoForOneCurrencyDto(
                "table",
                "currency",
                "code",
                List.of(actualExchangeRateDto));

        given(restTemplate.getForObject(CURRENCY_EXCHANGE_RATE, TableInfoForOneCurrencyDto.class, code)).willReturn(tableInfoForOneCurrencyDto);

        //when
        ExchangeRateDto expectedExchangeRateDto = currencyExchangeClient.getCurrentExchangeRateForCurrency(code);

        //then
        then(restTemplate).should().getForObject(CURRENCY_EXCHANGE_RATE, TableInfoForOneCurrencyDto.class, code);
        assertThat(expectedExchangeRateDto.getCurrency()).isEqualTo(actualExchangeRateDto.getCurrency());
        assertThat(expectedExchangeRateDto.getCode()).isEqualTo(actualExchangeRateDto.getCode());
        assertThat(expectedExchangeRateDto.getMid()).isEqualTo(actualExchangeRateDto.getMid());
    }
}