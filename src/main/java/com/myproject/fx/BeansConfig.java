package com.myproject.fx;

import com.myproject.fx.model.Currency;
import com.myproject.fx.model.CurrencyRate;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class BeansConfig {
    @Value("${fxcalculator.csv.file.name}")
    private String csvFileName;

    @Value("${fxcalculator.decimal.scale}")
    private Integer scale;

    @Value("${fxcalculator.decimal.rounding.mode}")
    private String roundingMode;

    @Bean
    public Map<Currency, BigDecimal> getCurrencyRates() {
        Map<Currency, BigDecimal> currencyRatesMap;
        try (Reader reader = new FileReader(new ClassPathResource(csvFileName).getFile())) {
            List<CurrencyRate> currencyRates = new CsvToBeanBuilder<CurrencyRate>(reader)
                    .withType(CurrencyRate.class)
                    .withIgnoreLeadingWhiteSpace(true).
                    build().
                    parse();
            currencyRatesMap = currencyRates.stream().collect(Collectors.toMap(CurrencyRate::getCurrency, CurrencyRate::getRate));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ApplicationContextException("Error while reading csv file", e);
        }

        return currencyRatesMap;
    }

    @Bean(name="scale")
    public Integer getScale(){
        return scale;
    }

    @Bean
    public RoundingMode getRoundingMode(){
        try {
            return RoundingMode.valueOf(roundingMode);
        } catch (IllegalArgumentException|NullPointerException e) {
            log.error(e.getMessage());
            throw new ApplicationContextException("Rounding mode is invalid or not provided in application.yml", e);
        }
    }
}
