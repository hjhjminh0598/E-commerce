package com.ecom.product.exchange_rate.service;

import com.ecom.product.exchange_rate.dto.ExchangeRateDTO;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

    Mono<ExchangeRateDTO> getExchangeRateData(String baseCurrency, String targetCurrency);

    Mono<Double> getExchangeRate(String baseCurrency, String targetCurrency);
}
