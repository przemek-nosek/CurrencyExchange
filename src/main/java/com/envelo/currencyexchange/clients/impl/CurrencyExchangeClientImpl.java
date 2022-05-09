package com.envelo.currencyexchange.clients.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.dto.TableInfoDto;
import com.envelo.currencyexchange.model.dto.TableInfoForOneCurrencyDto;
import com.envelo.currencyexchange.services.SystemLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATES_TABLE;

/**
 * An actual implementation class of {@link CurrencyExchangeClient}
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeClientImpl implements CurrencyExchangeClient {

    private final RestTemplate restTemplate;
    private final SystemLogService systemLogService;


    /**
     * <p>Returns a list of{@link ExchangeRateDto}</p>
     * <p>
     * Method used to connect to external API to fetch available currencies and it's exchange rates.
     * API call returns table consisting table info and it's exchange rates.
     *
     * @return List of ExchangeRate when successfully fetched data from the API.
     * @throws ExternalApiCallException when there was an error while connecting to the API>
     */
    @Override
    public List<ExchangeRateDto> getAvailableCurrencies() {
        systemLogService.saveLog(CURRENCY_EXCHANGE_RATES_TABLE, "getAvailableCurrencies()");
        TableInfoDto[] tableInfoDto = restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfoDto[].class);
        return Objects.requireNonNull(tableInfoDto)[0].getRates();
    }

    /**
     * Returns {@link ExchangeRateDto}
     * Method used to get current exchange rate for given currency
     *
     * @param currency to get exchange rate for
     * @return ExchangeRate for given currency
     * @throws ExternalApiCallException when there was an error while connecting to the API>
     */
    @Override
    public ExchangeRateDto getCurrentExchangeRateForCurrency(String currency) {
        try {
            systemLogService.saveLog(CURRENCY_EXCHANGE_RATE, String.format("getCurrentExchangeRateForCurrency(%s)", currency));
            TableInfoForOneCurrencyDto tableInfoForOneCurrencyDto = restTemplate.getForObject(CURRENCY_EXCHANGE_RATE, TableInfoForOneCurrencyDto.class, currency);
            ExchangeRateDto exchangeRateDto = Objects.requireNonNull(tableInfoForOneCurrencyDto).getRates().get(0);
            exchangeRateDto.setCurrency(tableInfoForOneCurrencyDto.getCurrency());
            exchangeRateDto.setCode(tableInfoForOneCurrencyDto.getCode());
            return exchangeRateDto;
        } catch (ExternalApiCallException ex) {
            systemLogService.saveLog(CURRENCY_EXCHANGE_RATE, String.format("ExternalApiCallException: %s", ex.getMessage() + currency));
            throw new ExternalApiCallException(ex.getMessage() + currency);
        }
    }
}
