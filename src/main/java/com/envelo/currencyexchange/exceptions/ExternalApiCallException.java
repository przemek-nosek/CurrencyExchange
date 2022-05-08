package com.envelo.currencyexchange.exceptions;

public class ExternalApiCallException extends RuntimeException {
    public ExternalApiCallException(String message) {
        super(message);
    }
}
