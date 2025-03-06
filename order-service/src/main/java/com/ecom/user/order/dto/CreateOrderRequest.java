package com.ecom.user.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {

    private UUID userId;

    private List<OrderItemRequest> items;
}
