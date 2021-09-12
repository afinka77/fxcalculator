package com.myproject.fx.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CurrencyRate {
    @CsvBindByName
    private Currency currency;
    @CsvBindByName
    private BigDecimal rate;
}
