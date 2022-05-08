package com.envelo.currencyexchange.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMessage {
    EXTERNAL_API_CALL_UNAVAILABLE("There was an error while trying to fetch data from: ");

    private final String message;

    public String getErrorMessage(String key) {
        return this.message + key;
    }

    public String getErrorMessage() {
        return this.message;
    }
}
