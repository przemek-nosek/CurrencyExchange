package com.envelo.currencyexchange.services.impl;

import com.envelo.currencyexchange.clients.CurrencyExchangeClient;
import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.mappers.CurrencyExchangeMapper;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceImplTest {

    @Mock
    private CurrencyExchangeClient currencyExchangeClient;

    @Mock
    private CurrencyExchangeMapper currencyExchangeMapper;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeServiceImpl;

    @Test
    void getAvailableCurrencies_shouldReturnAvailableCurrencyList_whenApiCallWasSuccessful() {
        //given
        given(currencyExchangeClient.getAvailableCurrencies()).willReturn(new ArrayList<>());
        given(currencyExchangeMapper.exchangeRateListToAvailableCurrencyDtoList(anyList())).willReturn(new ArrayList<>());

        //when
        currencyExchangeServiceImpl.getAvailableCurrencies();

        //then
        then(currencyExchangeClient).should().getAvailableCurrencies();
        then(currencyExchangeMapper).should().exchangeRateListToAvailableCurrencyDtoList(anyList());
    }
}