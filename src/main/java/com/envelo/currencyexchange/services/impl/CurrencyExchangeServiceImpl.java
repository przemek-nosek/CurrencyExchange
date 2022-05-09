package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.controllers.CurrencyExchangeController;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.dto.CurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.entities.SystemLog;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.repositories.SystemLogRepository;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import com.envelo.currencyexchange.validators.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
    private final SystemLogRepository systemLogRepository;

    /**
     * Method used to get available currencies {@link AvailableCurrencyDto}.
     *
     * @return list of AvailableCurrencyDto
     */
    @Override
    public List<AvailableCurrencyDto> getAvailableCurrencies() {
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeServiceImpl.class.getSimpleName())
                        .method("getAvailableCurrencies()")
                        .build()
        );
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
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeServiceImpl.class.getSimpleName())
                        .method(String.format("calculateCurrencyExchangeAmount(%s, %s, %s)", amount, from, to))
                        .build()
        );

        List<ExchangeRateDto> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        currencyValidator.validateGivenCurrencies(availableCurrencies, List.of(from, to));

        ExchangeRateDto fromCurrencyExchangeRateDto = currencyExchangeClient.getCurrentExchangeRateForCurrency(from);
        ExchangeRateDto toCurrencyExchangeRateDto = currencyExchangeClient.getCurrentExchangeRateForCurrency(to);

        BigDecimal result = calculateExchangeAmount(fromCurrencyExchangeRateDto.getMid(), toCurrencyExchangeRateDto.getMid(), amount);

        return new ExchangeCurrencyFromToDto(from, to, amount, result);
    }

    /**
     * Method used to get current rates for requested currencies as base currency.
     * @param currencyCodes requested currencies
     * @return {@link CurrencyDto} list with exchange rates for base currency
     */
    @Override
    public List<CurrencyDto> getCurrentRatesForCurrencies(List<String> currencyCodes) {
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeServiceImpl.class.getSimpleName())
                        .method(String.format("getCurrentRatesForCurrencies(%s)", currencyCodes))
                        .build()
        );
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
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeServiceImpl.class.getSimpleName())
                        .method(String.format("getExchangeAmountForRequestedCurrencies(%s, %s)",availableCurrencies, givenCurrencyCodesRates))
                        .build()
        );
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        CurrencyDto currencyDto;

        for (ExchangeRateDto givenCurrencyCodesRate : givenCurrencyCodesRates) {
            currencyDto = new CurrencyDto(givenCurrencyCodesRate.getCurrency(), givenCurrencyCodesRate.getCode());

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
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeServiceImpl.class.getSimpleName())
                        .method(String.format("getExchangeRateForRequestedCurrencies(%s, %s)", currencyCodes, availableCurrencies))
                        .build()
        );
        List<ExchangeRateDto> givenCurrencyCodesRates = new ArrayList<>();

        for (String currencyCode : currencyCodes) {
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
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeServiceImpl.class.getSimpleName())
                        .method(String.format("calculateExchangeAmount(%s, %s, %s)", fromExchangeRate, toExchangeRate, fromAmount))
                        .build()
        );
        int scale = 3;
        return fromAmount == null
                ? fromExchangeRate.divide(toExchangeRate, scale, RoundingMode.FLOOR)
                : fromExchangeRate.divide(toExchangeRate, scale, RoundingMode.FLOOR).multiply(fromAmount);
    }
}
