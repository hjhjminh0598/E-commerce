package com.gnt.ecom.exchange_rate.service;

import com.gnt.ecom.exchange_rate.dto.ExchangeRateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final String USD_CURRENCY = "USD";

    private final static String BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private final WebClient webClient;

    public ExchangeRateServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @Override
    public Double getExchangeRate(String baseCurrency, String targetCurrency) {
        return getExchangeRateMono(baseCurrency, targetCurrency).block();
    }

    @Override
    public Double getExchangeRateBaseUSD(String targetCurrency) {
        return getExchangeRate(USD_CURRENCY, targetCurrency);
    }

    public Mono<ExchangeRateDTO> getExchangeRateData(String baseCurrency, String targetCurrency) {
        return getExchangeRateMono(baseCurrency, targetCurrency)
                .map(rate -> ExchangeRateDTO.builder()
                        .rate(rate)
                        .base(baseCurrency)
                        .target(targetCurrency)
                        .build()
                );
    }

    @Override
    public Mono<Double> getExchangeRateMono(String baseCurrency, String targetCurrency) {
        return webClient.get()
                .uri(baseCurrency)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response == null || !response.containsKey("rates")) {
                        return Mono.error(new RuntimeException("Failed to fetch exchange rate"));
                    }

                    Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                    Object rateValue = rates.getOrDefault(targetCurrency, null);

                    if (rateValue == null) {
                        return Mono.error(new RuntimeException("Target currency not found"));
                    }

                    double exchangeRate;
                    if (rateValue instanceof Integer) {
                        exchangeRate = ((Integer) rateValue).doubleValue();
                    } else if (rateValue instanceof Double) {
                        exchangeRate = (Double) rateValue;
                    } else {
                        exchangeRate = (double) rateValue;
                    }

                    return Mono.just(exchangeRate);
                });
    }
}
