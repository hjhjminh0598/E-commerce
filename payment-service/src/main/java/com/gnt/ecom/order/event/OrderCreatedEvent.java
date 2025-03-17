package com.gnt.ecom.order.event;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderCreatedEvent {

    private String id;

    private UUID userId;

    private Double totalPrice;

    private Double totalLocalPrice;

    private String userCurrency;
}
