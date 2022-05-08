package com.envelo.currencyexchange.model.external;

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
public class TableInfo {
    private String table;
    private String no;
    private LocalDate tableDate;
    private LocalDate effectiveDate;
    private List<ExchangeRate> exchangeRates;
}
