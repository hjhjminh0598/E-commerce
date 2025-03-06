package com.ecom.user.order.service;

import com.ecom.user.base.BaseService;
import com.ecom.user.order.dto.OrderItemDTO;
import com.ecom.user.order.entity.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderItemService extends BaseService<OrderItem, UUID> {

    List<OrderItemDTO> getItemsByOrderId(UUID orderId);
}
