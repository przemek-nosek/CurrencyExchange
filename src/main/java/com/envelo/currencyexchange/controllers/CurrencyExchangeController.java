package com.envelo.currencyexchange.controllers;

import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.model.dto.ExchangeCurrencyFromToDto;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

import static com.envelo.currencyexchange.utils.SwaggerConstants.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency_exchanges")
@Validated
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @Operation(summary = "Get available currencies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available currencies",
                    content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AvailableCurrencyDto.class)))
            }),
            @ApiResponse(responseCode = "503", description = "External service is unavailable", content = @Content)
    })
    @GetMapping
    public List<AvailableCurrencyDto> getAvailableCurrencies() {
        return currencyExchangeService.getAvailableCurrencies();
    }

    @Operation(summary = "Calculate the amount from currency to desired currency.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculated amount from given currency to desired currency",
                    content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeCurrencyFromToDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid inputs", content = @Content),
            @ApiResponse(responseCode = "503", description = "External service is unavailable", content = @Content)
    })
    @GetMapping("/calculate")
    public ExchangeCurrencyFromToDto calculateCurrencyExchangeAmount(
            @DecimalMin(value = "0.00", inclusive = false) @Digits(integer = 10, fraction = 2)
                                    @RequestParam @Parameter(description = CALCULATE_AMOUNT_DESCRIPTION, example = "1.57") BigDecimal amount,
            @NotBlank @Size(min = 3, max = 3) @RequestParam @Parameter(description = CALCULATE_FROM_DESCRIPTION, example = "USD") String from,
            @NotBlank @Size(min = 3, max = 3) @RequestParam @Parameter(description = CALCULATE_TO_DESCRIPTION, example = "AUD") String to) {


        return currencyExchangeService.calculateCurrencyExchangeAmount(amount, from.toUpperCase(), to.toUpperCase());
    }
}
