package com.envelo.currencyexchange.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CurrencyDto {
    private String baseCurrency;
    private String baseCurrencyCode;
    private List<ExchangeRateDto> exchangeRateDtos = new ArrayList<>();

    public CurrencyDto(String baseCurrency, String baseCurrencyCode) {
        this.baseCurrency = baseCurrency;
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public void addExchangeRateDto(ExchangeRateDto exchangeRateDto) {
        this.exchangeRateDtos.add(exchangeRateDto);
    }
}
