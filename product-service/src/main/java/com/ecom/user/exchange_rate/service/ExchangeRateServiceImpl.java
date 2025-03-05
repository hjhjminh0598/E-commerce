package com.ecom.user.exchange_rate.service;

import com.ecom.user.exchange_rate.dto.ExchangeRateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final static String BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private final WebClient webClient;

    public ExchangeRateServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public ExchangeRateDTO getExchangeRateData(String baseCurrency, String targetCurrency) {
        return ExchangeRateDTO.builder()
                .rate(this.getExchangeRate(baseCurrency, targetCurrency))
                .base(baseCurrency)
                .target(targetCurrency)
                .build();
    }

    @Override
    public Double getExchangeRate(String baseCurrency, String targetCurrency) {
        Map response = webClient.get()
                .uri(baseCurrency)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("rates")) {
            throw new RuntimeException("Failed to fetch exchange rate");
        }

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        return rates.getOrDefault(targetCurrency, null);
    }
}
