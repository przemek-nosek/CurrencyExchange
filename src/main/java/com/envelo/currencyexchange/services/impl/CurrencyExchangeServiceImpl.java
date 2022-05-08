package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import com.envelo.currencyexchange.validators.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    /**
     * Method used to get available currencies {@link AvailableCurrencyDto}.
     *
     * @return list of AvailableCurrencyDto
     */
    @Override
    public List<AvailableCurrencyDto> getAvailableCurrencies() {
        List<ExchangeRate> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        return currencyExchangeMapper.exchangeRateListToAvailableCurrencyDtoList(availableCurrencies);
    }

    /**
     * Methods used to calculate given amount from one currency to another currency
     * Returns {@link ExchangeCurrencyFromToDto}
     *
     * @param amount given amount to calculate
     * @param from currency to calculate from
     * @param to currency to calculate to
     * @return ExchangeCurrencyFromToDto with calculated amount, given amount and currencies
     */
    @Override
    public ExchangeCurrencyFromToDto calculateCurrencyExchangeAmount(BigDecimal amount, String from, String to) {
        List<ExchangeRate> availableCurrencies = currencyExchangeClient.getAvailableCurrencies();

        currencyValidator.validateGivenCurrencies(availableCurrencies, from, to);

        ExchangeRate fromCurrencyExchangeRate = currencyExchangeClient.getCurrentExchangeRateForCurrency(from);
        ExchangeRate toCurrencyExchangeRate = currencyExchangeClient.getCurrentExchangeRateForCurrency(to);

        BigDecimal result = calculateExchangeAmount(fromCurrencyExchangeRate.getMid(), toCurrencyExchangeRate.getMid(), amount);

        return new ExchangeCurrencyFromToDto(from, to, amount, result);
    }

    /**
     * Calculates exchange rate for given currencies
     * @param fromExchangeRate exchange rate for 'from' currency
     * @param toExchangeRate exchange rate for 'to' currency
     * @param fromAmount given amount to exchange
     * @return BigDecimal calculated exchange rate
     */
    private BigDecimal calculateExchangeAmount(BigDecimal fromExchangeRate, BigDecimal toExchangeRate, BigDecimal fromAmount) {
        return fromExchangeRate.divide(toExchangeRate, 2, RoundingMode.FLOOR).multiply(fromAmount);
    }
}
