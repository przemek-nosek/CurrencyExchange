package com.envelo.currencyexchange.exceptions.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ValidationError {
    private String field;
    private String error;
}
