package com.envelo.currencyexchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableInfoDto {
    private String table;
    private String no;
    private LocalDate tradingDate;
    private LocalDate effectiveDate;
    private List<ExchangeRateDto> rates;
}
