package com.gnt.ecom.order.event;

import com.gnt.ecom.order.entity.Order;
import com.gnt.ecom.order.entity.PaymentMethod;
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

    private PaymentMethod method;

    public static OrderCreatedEvent of(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setId(order.getId());
        event.setUserId(order.getUserId());
        event.setTotalPrice(order.getTotalPrice());
        event.setTotalLocalPrice(order.getTotalLocalPrice());
        event.setUserCurrency(order.getUserCurrency());
        event.setMethod(order.getMethod());
        return event;
    }
}
