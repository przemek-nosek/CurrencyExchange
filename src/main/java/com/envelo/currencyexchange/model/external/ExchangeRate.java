package com.envelo.currencyexchange.model.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRate {
    private String currency;
    private String code;
    private BigDecimal bid;
    private BigDecimal ask;
}
