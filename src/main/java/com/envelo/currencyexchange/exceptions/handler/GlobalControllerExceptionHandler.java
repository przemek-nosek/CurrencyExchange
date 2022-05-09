package com.envelo.currencyexchange.exceptions.handler;

import com.envelo.currencyexchange.exceptions.ExternalApiCallException;
import com.envelo.currencyexchange.exceptions.InvalidCurrencyException;
import com.envelo.currencyexchange.exceptions.dto.ExceptionMessage;
import com.envelo.currencyexchange.exceptions.dto.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Global exception handler used to handle any exception that happens while using this application.
 */

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Method used to handle exception of type {@link ExternalApiCallException}
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
     * Method used to handle exception of type {@link InvalidCurrencyException}
     *
     * @param exception type of exception thrown
     * @return ResponseEntity returns ResponseEntity<ExceptionMessage> with HttpStatusCode.
     */
    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<ExceptionMessage> handleExternalApiCallException(InvalidCurrencyException exception) {
        ExceptionMessage errorMessage = getErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Constraint violation exception handler
     *
     * @param ex ConstraintViolationException
     * @return List<ValidationError> - list of ValidationError built
     * from set of ConstraintViolation
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ValidationError>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ValidationError> validationErrors = buildValidationErrors(ex.getConstraintViolations());

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Build list of ValidationError from set of ConstraintViolation
     *
     * @param violations Set<ConstraintViolation<?>> - Set
     * of parameterized ConstraintViolations
     * @return List<ValidationError> - list of validation errors
     */
    private List<ValidationError> buildValidationErrors(
            Set<ConstraintViolation<?>> violations) {
        return violations.
                stream().
                map(violation ->
                        ValidationError.builder().
                                field(
                                        Objects.requireNonNull(StreamSupport.stream(
                                                                violation.getPropertyPath().spliterator(), false).
                                                        reduce((first, second) -> second).
                                                        orElse(null)).
                                                toString()
                                ).
                                error(violation.getMessage()).
                                build())
                        .sorted(Comparator.comparing(ValidationError::getField))
                .collect(toList());
    }

    /**
     * Method to handle missing request parameter in url.
     *
     * @param ex type of caught exception
     * @param headers headers in request
     * @param status status code
     * @param request given request
     * @return ResponseEntity returns ResponseEntity with explanation what went wrong
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionMessage errorMessage = getErrorMessage(status, ex.getMessage());

        return new ResponseEntity<>(errorMessage, headers, status);
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
