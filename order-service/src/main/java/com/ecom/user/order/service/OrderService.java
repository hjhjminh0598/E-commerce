package com.ecom.user.order.service;

import com.ecom.user.base.BaseService;
import com.ecom.user.base.PageResponse;
import com.ecom.user.order.dto.CreateOrderRequest;
import com.ecom.user.order.dto.OrderDTO;
import com.ecom.user.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService extends BaseService<Order, UUID> {

    PageResponse<OrderDTO> getAllOrders(Pageable pageable);

    PageResponse<OrderDTO> getByUser(UUID userId, Pageable pageable);

    OrderDTO getById(UUID id);

    OrderDTO create(CreateOrderRequest request);
}
