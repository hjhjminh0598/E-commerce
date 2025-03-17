package com.gnt.ecom.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    private String productId;

    private Integer quantity;
}
