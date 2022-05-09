package com.envelo.currencyexchange.model.enums;

import lombok.RequiredArgsConstructor;

/**
 * Enum that contains various error messages that can happen while using this application.
 */

@RequiredArgsConstructor
public enum ErrorMessage {
    EXTERNAL_API_CALL_UNAVAILABLE("There was an error while trying to fetch data from: "),

    INVALID_CURRENCY_EXCEPTION("Given currency is not valid/available: ");

    private final String message;

    public String getErrorMessage(String key) {
        return this.message + key;
    }

    public String getErrorMessage() {
        return this.message;
    }
}
