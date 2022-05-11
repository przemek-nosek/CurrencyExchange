package com.envelo.currencyexchange.exceptions.handler;

import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

import static com.envelo.currencyexchange.model.enums.ErrorMessage.INVALID_CURRENCY_EXCEPTION;


public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new InvalidCurrencyException(INVALID_CURRENCY_EXCEPTION.getErrorMessage());
    }
}