package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.controllers.CurrencyExchangeController;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.dto.CurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import com.envelo.currencyexchange.services.SystemLogService;
import com.envelo.currencyexchange.validators.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * An actual implementation class of {@link CurrencyExchangeService}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final CurrencyExchangeClient currencyExchangeClient;
    private final CurrencyExchangeMapper currencyExchangeMapper;
    private final CurrencyValidator currencyValidator;
    private final SystemLogService systemLogService;

    /**
     * Method used to get available currencies {@link AvailableCurrencyDto}.
     *
     * @return list of AvailableCurrencyDto
     */
    @Override
    public List<AvailableCurrencyDto> getAvailableCurrencies() {
        systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(), "getAvailableCurrencies()");

        List<ExchangeRateDto> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        return currencyExchangeMapper.exchangeRateListToAvailableCurrencyDtoList(availableCurrencies);
    }

    /**
     * Methods used to calculate given amount from one currency to another currency
     * Returns {@link ExchangeCurrencyFromToDto}
     *
     * @param amount given amount to calculate
     * @param from   currency to calculate from
     * @param to     currency to calculate to
     * @return ExchangeCurrencyFromToDto with calculated amount, given amount and currencies
     */
    @Override
    public ExchangeCurrencyFromToDto calculateCurrencyExchangeAmount(BigDecimal amount, String from, String to) {
        systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(),
                String.format("calculateCurrencyExchangeAmount(%s, %s, %s)", amount, from, to));

        ExchangeRateDto fromCurrencyExchangeRateDto = currencyExchangeClient.getCurrentExchangeRateForCurrency(from);
        ExchangeRateDto toCurrencyExchangeRateDto = currencyExchangeClient.getCurrentExchangeRateForCurrency(to);

        BigDecimal result = calculateExchangeAmount(fromCurrencyExchangeRateDto.getMid(), toCurrencyExchangeRateDto.getMid(), amount);

        systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(),
                String.format("calculatedAmount: %s", result));


        return new ExchangeCurrencyFromToDto(from, to, amount, result);
    }

    /**
     * Method used to get current rates for requested currencies as base currency.
     * @param currencyCodes requested currencies
     * @return {@link CurrencyDto} list with exchange rates for base currency
     */
    @Override
    public List<CurrencyDto> getCurrentRatesForCurrencies(List<String> currencyCodes) {
        systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(),
                String.format("getCurrentRatesForCurrencies(%s)", currencyCodes));

        List<ExchangeRateDto> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        currencyValidator.validateGivenCurrencies(availableCurrencies, currencyCodes);

        List<ExchangeRateDto> givenCurrencyCodesRates = getExchangeRateForRequestedCurrencies(currencyCodes, availableCurrencies);

        return getExchangeAmountForRequestedCurrencies(availableCurrencies, givenCurrencyCodesRates);
    }

    /**
     * Method used to get exchange rates for requested currencies.
     *
     * @param availableCurrencies all available currencies fetched from api
     * @param givenCurrencyCodesRates exchange rates for requested currencies
     * @return list of {@link CurrencyDto} with exchange rates for requested currencies.
     */
    private List<CurrencyDto> getExchangeAmountForRequestedCurrencies(List<ExchangeRateDto> availableCurrencies, List<ExchangeRateDto> givenCurrencyCodesRates) {
        systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(), "getExchangeAmountForRequestedCurrencies()");

        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        CurrencyDto currencyDto;

        for (ExchangeRateDto givenCurrencyCodesRate : givenCurrencyCodesRates) {
            currencyDto = new CurrencyDto(givenCurrencyCodesRate.getCurrency(), givenCurrencyCodesRate.getCode());

            systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(), String.format("getExchangeAmountForRequestedCurrencies(givenCurrencyCodesRate: %s)", givenCurrencyCodesRate));

            for (ExchangeRateDto availableCurrency : availableCurrencies) {
                if (!givenCurrencyCodesRate.getCurrency().equals(availableCurrency.getCurrency())) {

                    BigDecimal exchangeAmount = calculateExchangeAmount(givenCurrencyCodesRate.getMid(), availableCurrency.getMid(), null);

                    currencyDto.addExchangeRateDto(
                            new ExchangeRateDto(
                                    availableCurrency.getCurrency(),
                                    availableCurrency.getCode(),
                                    exchangeAmount));
                }
            }

            currencyDtoList.add(currencyDto);
        }
        return currencyDtoList;
    }

    /**
     * Gets exchange rates for requested currencies
     * @param currencyCodes base currencies
     * @param availableCurrencies all available currencies fetched from api
     * @return list of {@link ExchangeRateDto} for requested currencies
     */
    private List<ExchangeRateDto> getExchangeRateForRequestedCurrencies(List<String> currencyCodes, List<ExchangeRateDto> availableCurrencies) {
        systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(), "getExchangeRateForRequestedCurrencies()");

        List<ExchangeRateDto> givenCurrencyCodesRates = new ArrayList<>();

        for (String currencyCode : currencyCodes) {
            systemLogService.saveLog(CurrencyExchangeController.class.getSimpleName(), String.format("getExchangeRateForRequestedCurrencies(currencyCode: %s)", currencyCode));
            for (ExchangeRateDto availableCurrency : availableCurrencies) {
                if (currencyCode.equals(availableCurrency.getCode())) {
                    givenCurrencyCodesRates.add(availableCurrency);
                }
            }
        }
        return givenCurrencyCodesRates;
    }

    /**
     * Calculates exchange rate for given currencies
     *
     * @param fromExchangeRate exchange rate for 'from' currency
     * @param toExchangeRate   exchange rate for 'to' currency
     * @param fromAmount       given amount to exchange
     * @return BigDecimal calculated exchange rate
     */
    private BigDecimal calculateExchangeAmount(BigDecimal fromExchangeRate, BigDecimal toExchangeRate, BigDecimal fromAmount) {
        int scale = 3;
        return fromAmount == null
                ? fromExchangeRate.divide(toExchangeRate, scale, RoundingMode.FLOOR)
                : fromExchangeRate.divide(toExchangeRate, scale, RoundingMode.FLOOR).multiply(fromAmount);
    }
}
