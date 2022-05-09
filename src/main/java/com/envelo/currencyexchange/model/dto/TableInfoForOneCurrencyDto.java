package com.envelo.currencyexchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableInfoForOneCurrencyDto {
    private String table;
    private String currency;
    private String code;
    private List<ExchangeRateDto> rates;
}
