package com.myproject.fx.service;

import com.myproject.fx.dto.CalcResult;
import com.myproject.fx.model.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static com.myproject.fx.model.Currency.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FxServiceTest {
    private static final Integer SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private Map<Currency, BigDecimal> currencyRates =
            Map.of(EUR, new BigDecimal(1),
                    USD, new BigDecimal("0.85"),
                    GBP, new BigDecimal("1.17"));
    private FxService fxService = new FxService(ROUNDING_MODE, SCALE, currencyRates);

    @Test
    void calculate_ok() {
        CalcResult result = fxService.calculate(new BigDecimal(100), USD, GBP);

        assertNotNull(result);
        assertEquals(new BigDecimal(100), result.getFromAmount());
        assertEquals(USD, result.getFromCurrency());
        assertEquals(new BigDecimal("72.65"), result.getToAmount());
        assertEquals(GBP, result.getToCurrency());
        assertEquals(new BigDecimal("0.7225"), result.getRate());
        assertNotNull(result.getDateTime());
    }


    @Test
    void calculateToAmount_fromEUR_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal calcResult = new BigDecimal("100").divide(USDCurrencyRate, SCALE, ROUNDING_MODE);
        assertEquals(new BigDecimal("117.65"), calcResult);

        BigDecimal result = fxService.calculateToAmount(EUR, new BigDecimal(100), USD);

        assertEquals(new BigDecimal("117.65"), result);
    }

    @Test
    void calculateToAmount_fromNonEURtoEUR_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal calcResult = new BigDecimal("100").multiply(USDCurrencyRate);
        assertEquals(new BigDecimal("85.00"), calcResult);

        BigDecimal result = fxService.calculateToAmount(USD, new BigDecimal(100), EUR);

        assertEquals(new BigDecimal("85.00"), result);
    }

    @Test
    void calculateToAmount_fromNonEURtoNonEUR_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal amountInEUR = new BigDecimal("100").multiply(USDCurrencyRate);
        assertEquals(new BigDecimal("85.00"), amountInEUR);
        BigDecimal GBPCurrencyRate = currencyRates.get(GBP);
        assertEquals(new BigDecimal("1.17"), GBPCurrencyRate);
        BigDecimal calcResult = amountInEUR.divide(GBPCurrencyRate, SCALE, ROUNDING_MODE);
        assertEquals(new BigDecimal("72.65"), calcResult);

        BigDecimal result = fxService.calculateToAmount(USD, new BigDecimal(100), GBP);

        assertEquals(new BigDecimal("72.65"), result);
    }

    @Test
    void getRate_toEUR_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);

        BigDecimal result = fxService.getRate(USD, EUR);

        assertEquals(new BigDecimal("0.85"), result);
    }

    @Test
    void getRate_fromEURtoNonEUR_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal calcResult = new BigDecimal(1).divide(USDCurrencyRate, SCALE, ROUNDING_MODE);
        assertEquals(new BigDecimal("1.18"), calcResult);

        BigDecimal result = fxService.getRate(EUR, USD);

        assertEquals(new BigDecimal("1.18"), result);
    }

    @Test
    void getRate_fromNonEURtoNonEUR_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal GBPCurrencyRate = currencyRates.get(GBP);
        assertEquals(new BigDecimal("1.17"), GBPCurrencyRate);
        BigDecimal calcResult = new BigDecimal(1).divide(USDCurrencyRate, SCALE, ROUNDING_MODE)
                .multiply(GBPCurrencyRate);
        assertEquals(new BigDecimal("1.3806"), calcResult);

        BigDecimal result = fxService.getRate(GBP, USD);

        assertEquals(new BigDecimal("1.3806"), result);
    }

    @Test
    void fromCurrencytoEURAmount_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal calcResult = new BigDecimal("100").multiply(USDCurrencyRate);
        assertEquals(new BigDecimal("85.00"), calcResult);

        BigDecimal result = fxService.fromCurrencytoEURAmount(new BigDecimal("100"), USD);

        assertEquals(new BigDecimal("85.00"), result);
    }

    @Test
    void fromEURtoCurrencyAmount_ok() {
        BigDecimal USDCurrencyRate = currencyRates.get(USD);
        assertEquals(new BigDecimal("0.85"), USDCurrencyRate);
        BigDecimal calcResult = new BigDecimal("100").divide(USDCurrencyRate, SCALE, ROUNDING_MODE);
        assertEquals(new BigDecimal("117.65"), calcResult);

        BigDecimal result = fxService.fromEURtoCurrencyAmount(new BigDecimal("100"), USD);

        assertEquals(new BigDecimal("117.65"), result);
    }
}