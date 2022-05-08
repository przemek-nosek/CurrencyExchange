package com.envelo.currencyexchange.validators;

import com.envelo.currencyexchange.enums.ErrorMessage;
import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.model.external.ExchangeRate;
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
    public void validateGivenCurrencies(List<ExchangeRate> availableCurrencies, String... currencies) {
        List<String> currencyCodes = extractCurrencyCodes(availableCurrencies);

        for (String currency : currencies) {
            if (!currencyCodes.contains(currency)) {
                throw new InvalidCurrencyException(INVALID_CURRENCY_EXCEPTION.getErrorMessage(currency));
            }
        }
    }

    /**
     * Method which extract currency codes from {@link ExchangeRate} list
     * @param availableCurrencies data fetched from api
     * @return list of extracted currency codes
     */
    private List<String> extractCurrencyCodes(List<ExchangeRate> availableCurrencies) {
        List<String> currencyCodes = new ArrayList<>();

        for (ExchangeRate availableCurrency : availableCurrencies) {
            currencyCodes.add(availableCurrency.getCode());
        }

        return currencyCodes;
    }
}
