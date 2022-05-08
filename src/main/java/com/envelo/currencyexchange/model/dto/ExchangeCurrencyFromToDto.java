package com.envelo.currencyexchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeCurrencyFromToDto {
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal amountToCalculate;
    private BigDecimal calculatedAmount;
}
