package com.envelo.currencyexchange.model.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRateDto {
    private String currency;
    private String code;
    private BigDecimal mid;

    @Override
    public String toString() {
        return "code = " + this.code;
    }
}
