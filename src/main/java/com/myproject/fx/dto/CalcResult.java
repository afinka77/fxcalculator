package com.myproject.fx.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myproject.fx.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class CalcResult {
    private BigDecimal fromAmount;
    private Currency fromCurrency;
    private BigDecimal toAmount;
    private Currency toCurrency;
    private BigDecimal rate;
    private Instant dateTime;
}
