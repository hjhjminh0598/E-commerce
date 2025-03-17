package com.gnt.ecom.exchange_rate.service;

import com.gnt.ecom.exchange_rate.dto.ExchangeRateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final static String BASE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private final WebClient webClient;

    public ExchangeRateServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<ExchangeRateDTO> getExchangeRateData(String baseCurrency, String targetCurrency) {
        return getExchangeRate(baseCurrency, targetCurrency)
                .map(rate -> ExchangeRateDTO.builder()
                        .rate(rate)
                        .base(baseCurrency)
                        .target(targetCurrency)
                        .build()
                );
    }

    @Override
    public Mono<Double> getExchangeRate(String baseCurrency, String targetCurrency) {
        return webClient.get()
                .uri(baseCurrency)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response == null || !response.containsKey("rates")) {
                        return Mono.error(new RuntimeException("Failed to fetch exchange rate"));
                    }

                    Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                    Double exchangeRate = rates.getOrDefault(targetCurrency, null);

                    return exchangeRate != null ? Mono.just(exchangeRate) : Mono.error(new RuntimeException("Target currency not found"));
                });
    }
}
