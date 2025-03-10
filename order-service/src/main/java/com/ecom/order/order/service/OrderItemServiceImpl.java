package com.ecom.order.order.service;

import com.ecom.order.base.BaseRepository;
import com.ecom.order.base.BaseServiceImpl;
import com.ecom.order.order.dto.OrderItemDTO;
import com.ecom.order.order.entity.OrderItem;
import com.ecom.order.order.repository.OrderItemRepository;
import com.ecom.order.product.service.ProductService;
import com.ecom.order.product.dto.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, UUID> implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    private final ProductService productService;

    protected OrderItemServiceImpl(BaseRepository<OrderItem, UUID> repository, OrderItemRepository orderItemRepository, ProductService productService) {
        super(repository);
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
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
        List<ProductResponse> productResponses = productService.getProductsByIds(productIds);

        Map<UUID, ProductResponse> productResponsesMap = productResponses.stream()
                .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));
        if (productResponsesMap.isEmpty()) {
            return;
        }

        orderItemDTOS.forEach(orderItemDTO -> {
            ProductResponse productResponse = productResponsesMap.get(orderItemDTO.getProductId());
            if (productResponse != null) {
                orderItemDTO.setProductName(productResponse.getName());
                orderItemDTO.setDescription(productResponse.getDescription());
            }
        });
    }
}
