package com.myproject.fx.api;

import com.myproject.fx.dto.CalcResult;
import com.myproject.fx.model.Currency;
import com.myproject.fx.service.FxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;

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
    public void calculate() throws Exception {
        CalcResult calcResult = CalcResult.builder()
                .fromAmount(new BigDecimal(1))
                .fromCurrency(Currency.valueOf("EUR"))
                .toAmount(new BigDecimal("0.8"))
                .toCurrency(Currency.valueOf("USD"))
                .rate(new BigDecimal("0.8"))
                .build();
        when(fxService.calculate(new BigDecimal(1),"EUR","USD")).thenReturn(calcResult);

        mvc.perform(get("/v1/fx/calculate?amount=1&fromCurrency=EUR&toCurrency=USD")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fromAmount", is(1)))
                .andExpect(jsonPath("fromCurrency", is("EUR")))
                .andExpect(jsonPath("toAmount", is(0.8)))
                .andExpect(jsonPath("toCurrency", is("USD")))
                .andExpect(jsonPath("rate", is(0.8)));
    }
}