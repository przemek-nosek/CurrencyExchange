package com.envelo.currencyexchange.validators;

import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.entities.SystemLog;
import com.envelo.currencyexchange.repositories.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.envelo.currencyexchange.enums.ErrorMessage.INVALID_CURRENCY_EXCEPTION;

@Component
@RequiredArgsConstructor
public class CurrencyValidator {

    private final SystemLogRepository systemLogRepository;

    /**
     * Method validates if given currencies are valid.
     *
     * @param availableCurrencies data fetched from api
     * @param currencies          requested currencies
     * @throws InvalidCurrencyException throws {@link InvalidCurrencyException} when given currencies are invalid
     */
    public void validateGivenCurrencies(List<ExchangeRateDto> availableCurrencies, List<String> currencies) {
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyValidator.class.getSimpleName())
                        .method(String.format("validateGivenCurrencies(%s, %s)", currencies, availableCurrencies))
                        .build()
        );
        List<String> currencyCodes = extractCurrencyCodes(availableCurrencies);

        for (String currency : currencies) {
            if (!currencyCodes.contains(currency.toUpperCase())) {
                throw new InvalidCurrencyException(INVALID_CURRENCY_EXCEPTION.getErrorMessage(currency));
            }
        }
    }

    /**
     * Method which extract currency codes from {@link ExchangeRateDto} list
     *
     * @param availableCurrencies data fetched from api
     * @return list of extracted currency codes
     */
    private List<String> extractCurrencyCodes(List<ExchangeRateDto> availableCurrencies) {
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyValidator.class.getSimpleName())
                        .method(String.format("validateGivenCurrencies(%s)", availableCurrencies))
                        .build()
        );

        List<String> currencyCodes = new ArrayList<>();

        for (ExchangeRateDto availableCurrency : availableCurrencies) {
            currencyCodes.add(availableCurrency.getCode());
        }

        return currencyCodes;
    }
}
