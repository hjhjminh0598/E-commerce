package com.ecom.product.exchange_rate.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExchangeRateDTO {

    private String base;

    private String target;

    private Double rate;
}
