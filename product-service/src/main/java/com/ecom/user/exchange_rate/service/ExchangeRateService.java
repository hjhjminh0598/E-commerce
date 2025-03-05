package com.ecom.user.exchange_rate.service;

import com.ecom.user.exchange_rate.dto.ExchangeRateDTO;

public interface ExchangeRateService {

    ExchangeRateDTO getExchangeRateData(String baseCurrency, String targetCurrency);

    Double getExchangeRate(String baseCurrency, String targetCurrency);
}
