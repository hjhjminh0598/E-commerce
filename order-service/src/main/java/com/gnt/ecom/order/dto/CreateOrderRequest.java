package com.gnt.ecom.order.dto;

import com.gnt.ecom.order.entity.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {

    private UUID userId;

    private List<OrderItemRequest> items;

    private PaymentMethod paymentMethod = PaymentMethod.CASH;
}
