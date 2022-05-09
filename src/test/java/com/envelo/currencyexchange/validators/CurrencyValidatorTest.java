package com.envelo.currencyexchange.validators;

import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.envelo.currencyexchange.enums.ErrorMessage.INVALID_CURRENCY_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyValidatorTest {

    private final CurrencyValidator currencyValidator = new CurrencyValidator();

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