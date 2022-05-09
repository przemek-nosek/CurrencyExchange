package com.envelo.currencyexchange.clients.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.controllers.CurrencyExchangeController;
import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.model.dto.ExchangeRateDto;
import com.envelo.currencyexchange.model.dto.TableInfoDto;
import com.envelo.currencyexchange.model.dto.TableInfoForOneCurrencyDto;
import com.envelo.currencyexchange.model.entities.SystemLog;
import com.envelo.currencyexchange.repositories.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static com.envelo.currencyexchange.enums.ErrorMessage.EXTERNAL_API_CALL_UNAVAILABLE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATE;
import static com.envelo.currencyexchange.utils.CurrencyExchangeConstants.CURRENCY_EXCHANGE_RATES_TABLE;

/**
 * An actual implementation class of {@link CurrencyExchangeClient}
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeClientImpl implements CurrencyExchangeClient { // TODO: LOG CALL IN SYSTEM AND SAVE THEM TO DB.

    private final RestTemplate restTemplate;
    private final SystemLogRepository systemLogRepository;


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
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeClientImpl.class.getSimpleName())
                        .method("getAvailableCurrencies() " + CURRENCY_EXCHANGE_RATES_TABLE)
                        .build()
        );
        TableInfoDto[] tableInfoDto = restTemplate.getForObject(CURRENCY_EXCHANGE_RATES_TABLE, TableInfoDto[].class);

        if (tableInfoDto == null || tableInfoDto.length == 0) {
            systemLogRepository.save(
                    SystemLog.builder()
                            .timeStamp(LocalDateTime.now())
                            .className(ExternalApiCallException.class.getSimpleName())
                            .method("getAvailableCurrencies() " + EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATES_TABLE))
                            .build()
            );
            throw new ExternalApiCallException(EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATES_TABLE));
        }

        return tableInfoDto[0].getRates();
    }

    /**
     * Returns {@link ExchangeRateDto}
     * Method used to get current exchange rate for given currency
     * @param currency to get exchange rate for
     * @return ExchangeRate for given currency
     * @throws ExternalApiCallException when there was an error while connecting to the API>
     */
    @Override
    public ExchangeRateDto getCurrentExchangeRateForCurrency(String currency) {
        systemLogRepository.save(
                SystemLog.builder()
                        .timeStamp(LocalDateTime.now())
                        .className(CurrencyExchangeClientImpl.class.getSimpleName())
                        .method("getCurrentExchangeRateForCurrency() " + CURRENCY_EXCHANGE_RATE)
                        .build()
        );

        TableInfoForOneCurrencyDto tableInfoForOneCurrencyDto = restTemplate.getForObject(CURRENCY_EXCHANGE_RATE, TableInfoForOneCurrencyDto.class, currency);

        if (tableInfoForOneCurrencyDto == null) {
            systemLogRepository.save(
                    SystemLog.builder()
                            .timeStamp(LocalDateTime.now())
                            .className(ExternalApiCallException.class.getSimpleName())
                            .method("getCurrentExchangeRateForCurrency() " + EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATE))
                            .build()
            );
            throw new ExternalApiCallException(EXTERNAL_API_CALL_UNAVAILABLE.getErrorMessage(CURRENCY_EXCHANGE_RATE));
        }

        ExchangeRateDto exchangeRateDto = tableInfoForOneCurrencyDto.getRates().get(0);
        exchangeRateDto.setCurrency(tableInfoForOneCurrencyDto.getCurrency());
        exchangeRateDto.setCode(tableInfoForOneCurrencyDto.getCode());
        return exchangeRateDto;
    }
}
