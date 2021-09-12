package com.myproject.fx.service;

import com.myproject.fx.dto.CalcResult;
import com.myproject.fx.model.Currency;
import com.myproject.fx.validator.CurrencyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;

import static com.myproject.fx.model.Currency.EUR;

@Service
public class FxService {
    private RoundingMode roundingMode;
    private Integer scale;
    private Map<Currency, BigDecimal> currencyRates;
    private CurrencyValidator validator;

    public FxService(@Autowired RoundingMode roundingMode,
            @Autowired @Qualifier("scale") Integer scale,
            @Autowired Map<Currency, BigDecimal> currencyRates) {
        this.roundingMode = roundingMode;
        this.scale = scale;
        this.currencyRates = currencyRates;
    }

    public CalcResult calculate(BigDecimal amount,
                                Currency fromCurrency,
                                Currency toCurrency) {
        return CalcResult.builder()
                .fromAmount(amount)
                .fromCurrency(fromCurrency)
                .toAmount(calculateToAmount(fromCurrency, amount, toCurrency))
                .toCurrency(toCurrency)
                .rate(getRate(fromCurrency, toCurrency))
                .dateTime(Instant.now(Clock.systemUTC()))
                .build();
    }

    private BigDecimal calculateToAmount(Currency fromCurrency, BigDecimal fromAmount, Currency toCurrency) {
        if (EUR.equals(fromCurrency)) {
            return fromEURtoCurrencyAmount(fromAmount, toCurrency);
        } else {
            BigDecimal fromAmountInEUR = fromCurrencytoEURAmount(fromAmount, fromCurrency);
            return fromEURtoCurrencyAmount(fromAmountInEUR, toCurrency);
        }
    }

    private BigDecimal getRate(Currency fromCurrency, Currency toCurrency) {
        if (EUR.equals(toCurrency)) {
            return currencyRates.get(fromCurrency);
        } else {
            return new BigDecimal(1).divide(currencyRates.get(toCurrency), scale, roundingMode)
                                        .multiply(currencyRates.get(fromCurrency));
        }
    }

    private BigDecimal fromCurrencytoEURAmount(BigDecimal fromAmount, Currency fromCurrency) {
        return fromAmount.multiply(currencyRates.get(fromCurrency));
    }

    private BigDecimal fromEURtoCurrencyAmount(BigDecimal fromAmount, Currency toCurrency) {
        return fromAmount.divide(currencyRates.get(toCurrency), scale, roundingMode);
    }
}
