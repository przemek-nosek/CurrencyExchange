package com.envelo.currencyexchange.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SwaggerConstants {
    public static final String CALCULATE_AMOUNT_DESCRIPTION = "Amount to be calculated from. Must be higher than 0.00, max integral digits = 10, max fraction digits = 2";

    public static final String CALCULATE_FROM_DESCRIPTION = "Currency from which you calculate, must be 3 characters long. See available currency codes at /api/currency_exchanges";

    public static final String CALCULATE_TO_DESCRIPTION = "Currency to which you calculate, must be 3 characters long See available currency codes at /api/currency_exchanges";

    public static final String EXCHANGE_RATES_FOR_CURRENCY_CODES = "Currencies to get exchange rates for, separated by comma, must be 3 characters long See available currency codes at /api/currency_exchanges";
}
