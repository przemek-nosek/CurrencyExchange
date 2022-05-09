package com.envelo.currencyexchange.validators;

import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.envelo.currencyexchange.enums.ErrorMessage.INVALID_CURRENCY_EXCEPTION;

@Component
public class CurrencyValidator {

    /**
     * Method validates if given currencies are valid.
     * @param availableCurrencies data fetched from api
     * @param currencies requested currencies
     * @throws InvalidCurrencyException throws {@link InvalidCurrencyException} when given currencies are invalid
     */
    public void validateGivenCurrencies(List<ExchangeRateDto> availableCurrencies, List<String> currencies) {
        List<String> currencyCodes = extractCurrencyCodes(availableCurrencies);

        for (String currency : currencies) {
            if (!currencyCodes.contains(currency.toUpperCase())) {
                throw new InvalidCurrencyException(INVALID_CURRENCY_EXCEPTION.getErrorMessage(currency));
            }
        }
    }

    /**
     * Method which extract currency codes from {@link ExchangeRateDto} list
     * @param availableCurrencies data fetched from api
     * @return list of extracted currency codes
     */
    private List<String> extractCurrencyCodes(List<ExchangeRateDto> availableCurrencies) {
        List<String> currencyCodes = new ArrayList<>();

        for (ExchangeRateDto availableCurrency : availableCurrencies) {
            currencyCodes.add(availableCurrency.getCode());
        }

        return currencyCodes;
    }
}
