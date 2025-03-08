package com.ecom.order.order.service;

import com.ecom.order.base.BaseService;
import com.ecom.order.base.PageResponse;
import com.ecom.order.order.dto.CreateOrderRequest;
import com.ecom.order.order.dto.OrderDTO;
import com.ecom.order.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService extends BaseService<Order, UUID> {

    PageResponse<OrderDTO> getAllOrders(Pageable pageable);

    PageResponse<OrderDTO> getByUser(UUID userId, Pageable pageable);

    OrderDTO getById(UUID id);

    OrderDTO create(CreateOrderRequest request);
}
