package com.myproject.fx.api;

import com.myproject.fx.dto.CalcResult;
import com.myproject.fx.service.FxService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/fx", produces = MediaType.APPLICATION_JSON_VALUE)
public class FxController {

    private FxService fxService;

    @GetMapping("/calculate")
    public CalcResult calculate(@RequestParam BigDecimal amount,
                                @RequestParam String fromCurrency,
                                @RequestParam String toCurrency) {
        return fxService.calculate(amount, fromCurrency, toCurrency);
    }
}
