package com.envelo.currencyexchange.validators;

import com.envelo.currencyexchange.controllers.CurrencyExchangeController;
import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.services.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.envelo.currencyexchange.model.enums.ErrorMessage.INVALID_CURRENCY_EXCEPTION;

@Component
@RequiredArgsConstructor
public class CurrencyValidator {

    private final SystemLogService systemLogService;

    /**
     * Method validates if given currencies are valid.
     *
     * @param availableCurrencies data fetched from api
     * @param currencies          requested currencies
     * @throws InvalidCurrencyException throws {@link InvalidCurrencyException} when given currencies are invalid
     */
    public void validateGivenCurrencies(List<ExchangeRateDto> availableCurrencies, List<String> currencies) {
        List<String> currencyCodes = extractCurrencyCodes(availableCurrencies);

        StringBuilder invalidCurrencies = new StringBuilder();

        for (String currency : currencies) {
            if (!currencyCodes.contains(currency.toUpperCase())) {
                invalidCurrencies.append(currency).append(",");
            }
        }
        if (!invalidCurrencies.isEmpty()) {
            String errorMessage = INVALID_CURRENCY_EXCEPTION.getErrorMessage(invalidCurrencies.substring(0, invalidCurrencies.length() - 1));
            systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(), String.format("validateGivenCurrencies(%s )", errorMessage));
            throw new InvalidCurrencyException(errorMessage);
        }
    }

    /**
     * Method which extract currency codes from {@link ExchangeRateDto} list
     *
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
