package com.envelo.currencyexchange.exceptions.handler;

import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.exceptions.dto.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler used to handle any exception that happens while using this application.
 */

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Method used to handle exception of Type {@link ExternalApiCallException}
     *
     * @param exception ExternalApiCallException dedicated class for this kind of exception
     * @return ResponseEntity returns ResponseEntity<ExceptionMessage> with HttpStatusCode.
     */

    @ExceptionHandler(ExternalApiCallException.class)
    public ResponseEntity<ExceptionMessage> handleExternalApiCallException(ExternalApiCallException exception) {
        ExceptionMessage errorMessage = getErrorMessage(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Method used to create object of ExceptionMessage.
     *
     * @param httpStatus status of response
     * @param message message to describe what went wrong
     * @return ExceptionMessage data class with specifics of given exception
     */

    private ExceptionMessage getErrorMessage(HttpStatus httpStatus, String message) {
        return new ExceptionMessage(httpStatus, LocalDateTime.now(), message);
    }
}
