package com.ecom.user.order.service;

import com.ecom.user.base.BaseRepository;
import com.ecom.user.base.BaseServiceImpl;
import com.ecom.user.order.dto.OrderItemDTO;
import com.ecom.user.order.entity.OrderItem;
import com.ecom.user.order.repository.OrderItemRepository;
import com.ecom.user.product.ProductServiceClient;
import com.ecom.user.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, UUID> implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    private final ProductServiceClient productServiceClient;

    protected OrderItemServiceImpl(BaseRepository<OrderItem, UUID> repository, OrderItemRepository orderItemRepository, ProductServiceClient productServiceClient) {
        super(repository);
        this.orderItemRepository = orderItemRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public List<OrderItemDTO> getByOrderId(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        if (orderItems.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> productIds = new ArrayList<>();
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        orderItems.forEach(orderItem -> {
            productIds.add(orderItem.getProductId().toString());
            orderItemDTOS.add(OrderItemDTO.of(orderItem));
        });
        addProductDetailInfo(orderItemDTOS, productIds);

        return orderItemDTOS;
    }

    private void addProductDetailInfo(List<OrderItemDTO> orderItemDTOS, List<String> productIds) {
        Map<UUID, ProductResponse> productResponses = productServiceClient.getProductByIds(productIds).stream()
                .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
        if (productResponses.isEmpty()) {
            return;
        }

        orderItemDTOS.forEach(orderItemDTO -> {
            ProductResponse productResponse = productResponses.get(orderItemDTO.getProductId());
            if (productResponse != null) {
                orderItemDTO.setProductName(productResponse.getName());
                orderItemDTO.setDescription(productResponse.getDescription());
            }
        });
    }
}
