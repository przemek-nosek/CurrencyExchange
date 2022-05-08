package com.envelo.currencyexchange.controllers;

import com.envelo.currencyexchange.model.dto.AvailableCurrencyDto;
import com.envelo.currencyexchange.services.CurrencyExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currency_exchanges")
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @Operation(summary = "Get available currencies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available currencies",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AvailableCurrencyDto.class)))}),
            @ApiResponse(responseCode = "503", description = "External service is unavailable",
                    content = @Content)
    })
    @GetMapping
    public List<AvailableCurrencyDto> getAvailableCurrencies() {
        return currencyExchangeService.getAvailableCurrencies();
    }
}
