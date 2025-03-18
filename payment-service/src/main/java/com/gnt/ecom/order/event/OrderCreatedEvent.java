package com.gnt.ecom.order.event;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderCreatedEvent implements Serializable {

    private UUID id;

    private UUID userId;

    private BigDecimal totalPrice;

    private BigDecimal totalLocalPrice;

    private String userCurrency;
}
