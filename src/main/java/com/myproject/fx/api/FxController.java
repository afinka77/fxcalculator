package com.myproject.fx.api;

import com.myproject.fx.dto.CalcResult;
import com.myproject.fx.model.Currency;
import com.myproject.fx.service.FxService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/fx", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class FxController {

    private FxService fxService;

    @GetMapping("/calculate")
    public CalcResult calculate(@RequestParam("amount") @Positive BigDecimal amount,
                                @RequestParam("fromCurrency") Currency fromCurrency,
                                @RequestParam("toCurrency") Currency toCurrency) {
        return fxService.calculate(amount, fromCurrency, toCurrency);
    }
}
