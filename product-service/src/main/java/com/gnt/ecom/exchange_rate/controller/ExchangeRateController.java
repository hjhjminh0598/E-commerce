package com.gnt.ecom.exchange_rate.controller;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.exchange_rate.dto.ExchangeRateDTO;
import com.gnt.ecom.exchange_rate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<BaseResponse<ExchangeRateDTO>> getExchangeRate(@RequestParam String baseCurrency,
                                                                         @RequestParam String targetCurrency) {
        try {
            Mono<ExchangeRateDTO> exchangeRateDTOMono = exchangeRateService.getExchangeRateData(baseCurrency, targetCurrency);
            return ResponseEntity.ok(BaseResponse.success(exchangeRateDTOMono.block()));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }
}
