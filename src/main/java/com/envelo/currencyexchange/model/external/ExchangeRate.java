package com.envelo.currencyexchange.model.external;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExchangeRate {
    private String currency; // TODO: JSON PROPERTY
    private String code;
    private BigDecimal mid;
}
