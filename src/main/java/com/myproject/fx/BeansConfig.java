package com.myproject.fx;

import com.myproject.fx.model.Currency;
import com.myproject.fx.model.CurrencyRate;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class BeansConfig {
    public static final String CSV_FILE_NAME = "data.csv";

    @Bean
    public Map<Currency, BigDecimal> getCurrencyRates() {
        Map<Currency, BigDecimal> currencyRatesMap;
        try (Reader reader = new FileReader(new ClassPathResource(CSV_FILE_NAME).getFile())) {
            List<CurrencyRate> currencyRates = new CsvToBeanBuilder(reader)
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
}
