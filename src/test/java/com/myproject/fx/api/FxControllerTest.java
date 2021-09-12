package com.myproject.fx.api;

import com.myproject.fx.dto.CalcResult;
import com.myproject.fx.service.FxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

import static com.myproject.fx.model.Currency.*;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = FxController.class)
class FxControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FxService fxService;

    @Test
    public void calculate_valid_200() throws Exception {
        CalcResult calcResult = CalcResult.builder()
                .fromAmount(new BigDecimal(25))
                .fromCurrency(USD)
                .toAmount(new BigDecimal("18.162393162393162393"))
                .toCurrency(GBP)
                .rate(new BigDecimal("0.72649572649572649585"))
                .dateTime(Instant.parse("2021-09-15T10:15:30Z"))
                .build();
        when(fxService.calculate(new BigDecimal(25), USD, GBP)).thenReturn(calcResult);

        mvc.perform(get("/v1/fx/calculate?amount=25&fromCurrency=USD&toCurrency=GBP")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fromAmount", is(25)))
                .andExpect(jsonPath("fromCurrency", is("USD")))
                .andExpect(jsonPath("toAmount", is(new BigDecimal("18.162393162393162393"))))
                .andExpect(jsonPath("toCurrency", is("GBP")))
                .andExpect(jsonPath("rate", is(new BigDecimal("0.72649572649572649585"))))
                .andExpect(jsonPath("dateTime", is("2021-09-15T10:15:30Z")));
    }

    @Test
    public void calculate_missingParam_400() throws Exception {
        mvc.perform(get("/v1/fx/calculate?amount=25&fromCurrency=USD")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("dateTime", is(anything())))
                .andExpect(jsonPath("status", is("BAD_REQUEST")))
                .andExpect(jsonPath("message", is("Required request parameter 'toCurrency' for method " +
                        "parameter type Currency is not present")))
                .andExpect(jsonPath("errors", is(Arrays.asList("Required request parameter 'toCurrency' for" +
                        " method parameter type Currency is not present"))));
    }

    @Test
    public void calculate_invalidCurrency_400() throws Exception {
        mvc.perform(get("/v1/fx/calculate?amount=25&fromCurrency=USD&toCurrency=ABC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("dateTime", is(anything())))
                .andExpect(jsonPath("status", is("BAD_REQUEST")))
                .andExpect(jsonPath("message", is("Failed to convert value of type 'java.lang.String' " +
                        "to required type 'com.myproject.fx.model.Currency'; nested exception is " +
                        "org.springframework.core.convert.ConversionFailedException: Failed to convert from type" +
                        " [java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam " +
                        "com.myproject.fx.model.Currency] for value 'ABC'; nested exception is" +
                        " java.lang.IllegalArgumentException: No enum constant com.myproject.fx.model.Currency.ABC")))
                .andExpect(jsonPath("errors", is(Arrays.asList("Parameter 'toCurrency' has invalid value 'ABC'. " +
                        "Available values are: 'EUR','USD','GBP','BTC','ETH','FKE'"))));
    }

    @Test
    public void calculate_negativeAmount_400() throws Exception {
        mvc.perform(get("/v1/fx/calculate?amount=-1&fromCurrency=USD&toCurrency=EUR")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("dateTime", is(anything())))
                .andExpect(jsonPath("status", is("BAD_REQUEST")))
                .andExpect(jsonPath("message", is("calculate.amount: must be greater than 0")))
                .andExpect(jsonPath("errors", is(Arrays.asList("Request parameter 'calculate.amount' has " +
                        "invalid value '-1', must be greater than 0"))));
    }
}