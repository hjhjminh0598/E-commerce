package com.ecom.user.exchange_rate.service;

import com.ecom.user.exchange_rate.dto.ExchangeRateDTO;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

    Double getExchangeRate(String baseCurrency, String targetCurrency);

    Double getExchangeRateBaseUSD(String targetCurrency);

    Mono<ExchangeRateDTO> getExchangeRateData(String baseCurrency, String targetCurrency);

    Mono<Double> getExchangeRateMono(String baseCurrency, String targetCurrency);

}
