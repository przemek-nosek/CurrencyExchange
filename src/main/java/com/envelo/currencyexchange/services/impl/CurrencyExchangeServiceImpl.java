package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.external.ExchangeRate;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
