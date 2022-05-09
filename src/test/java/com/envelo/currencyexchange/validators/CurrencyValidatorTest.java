package com.envelo.currencyexchange.validators;

import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.services.impl.SystemLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.envelo.currencyexchange.model.enums.ErrorMessage.INVALID_CURRENCY_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class CurrencyValidatorTest {

    @Mock
    private SystemLogServiceImpl systemLogService;

    @InjectMocks
    private CurrencyValidator currencyValidator;

    @Test
    void validateGivenCurrencies_shouldNotThrowAnything_whenCurrenciesAreValid() {
        //given
        ExchangeRateDto exchangeRateDto = getExchangeRate();
        String currency = "USD";

        //when
        //then
        currencyValidator.validateGivenCurrencies(List.of(exchangeRateDto), List.of(currency));
    }

    @Test
    void validateGivenCurrencies_shouldThrowAnything_whenCurrenciesAreInValid() {
        //given
        willDoNothing().given(systemLogService).saveLog(anyString(), anyString());
        ExchangeRateDto exchangeRateDto = getExchangeRate();
        String invalidCurrency = "USo";

        //when
        //then
        assertThatThrownBy(() -> currencyValidator.validateGivenCurrencies(List.of(exchangeRateDto), List.of(invalidCurrency)))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessage(INVALID_CURRENCY_EXCEPTION.getErrorMessage(invalidCurrency));
    }

    private ExchangeRateDto getExchangeRate() {
        return new ExchangeRateDto("dolar", "USD", BigDecimal.TEN);
    }
}