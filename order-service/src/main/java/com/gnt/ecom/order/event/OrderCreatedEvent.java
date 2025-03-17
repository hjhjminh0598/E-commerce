package com.gnt.ecom.order.event;

import com.gnt.ecom.order.entity.Order;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderCreatedEvent {

    private String id;

    private UUID userId;

    private Double totalPrice;

    private Double totalLocalPrice;

    private String userCurrency;

    public static OrderCreatedEvent of(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setId(order.getId().toString());
        event.setUserId(order.getUserId());
        event.setTotalPrice(order.getTotalPrice());
        event.setTotalLocalPrice(order.getTotalLocalPrice());
        event.setUserCurrency(order.getUserCurrency());
        return event;
    }
}
