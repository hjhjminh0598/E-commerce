package com.gnt.ecom.order.dto;

import com.gnt.ecom.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    private UUID id;

    private Double totalPrice;

    private Double totalLocalPrice;

    private List<OrderItemDTO> orderItems;

    private LocalDateTime updatedAt;

    public static OrderDTO of(Order order, List<OrderItemDTO> orderItems) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setTotalLocalPrice(order.getTotalLocalPrice());
        orderDTO.setOrderItems(orderItems);
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        return orderDTO;
    }
}
