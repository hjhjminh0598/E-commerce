package com.ecom.user.order.service;

import com.ecom.user.base.BaseRepository;
import com.ecom.user.base.BaseResponse;
import com.ecom.user.base.BaseServiceImpl;
import com.ecom.user.base.PageResponse;
import com.ecom.user.exchange_rate.service.ExchangeRateService;
import com.ecom.user.order.dto.CreateOrderRequest;
import com.ecom.user.order.dto.OrderDTO;
import com.ecom.user.order.dto.OrderItemRequest;
import com.ecom.user.order.entity.Order;
import com.ecom.user.order.entity.OrderItem;
import com.ecom.user.product.ProductServiceClient;
import com.ecom.user.product.dto.ProductResponse;
import com.ecom.user.user.UserServiceClient;
import com.ecom.user.user.dto.UserResponse;
import com.ecom.user.utils.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, UUID> implements OrderService {

    private final UserServiceClient userServiceClient;

    private final ProductServiceClient productServiceClient;

    private final ExchangeRateService exchangeRateService;

    protected OrderServiceImpl(BaseRepository<Order, UUID> repository, ProductServiceClient productServiceClient, UserServiceClient userServiceClient, ExchangeRateService exchangeRateService) {
        super(repository);
        this.productServiceClient = productServiceClient;
        this.userServiceClient = userServiceClient;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public PageResponse<OrderDTO> getAllOrders(Pageable pageable) {
        return PageResponse.from(super.findAll(pageable).map(OrderDTO::of));
    }

    @Override
    public OrderDTO getById(UUID id) {
        return super.findById(id).map(OrderDTO::of).orElse(null);
    }

    @Override
    public OrderDTO create(CreateOrderRequest request) {
        validateOrderRequest(request);

        UserResponse user = getUserById(request.getUserId());
        Order order = new Order();
        order.setUserId(user.getId());
        order.setUserCurrency(user.getCurrency());

        List<ProductResponse> products = fetchProducts(request.getItems());
        Map<String, ProductResponse> productMap = products.stream()
                .collect(Collectors.toMap(p -> p.getId().toString(), Function.identity()));

        List<OrderItem> items = mapToOrderItems(order, productMap, request.getItems());

        order.setItems(items);
        setPrice(order, items);

        return OrderDTO.of(super.save(order));
    }

    private void setPrice(Order order, List<OrderItem> items) {
        order.setTotalPrice(items.stream()
                .filter(i -> i.getPrice() >= 0 && i.getQuantity() > 0)
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum());
        order.setTotalLocalPrice(order.getTotalPrice() * exchangeRateService.getExchangeRateBaseUSD(order.getUserCurrency()));
    }

    private void validateOrderRequest(CreateOrderRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("User id is required");
        }
        if (CollectionUtils.isEmpty(request.getItems())) {
            throw new RuntimeException("Order items are required");
        }

        for (OrderItemRequest item : request.getItems()) {
            if (item.getProductId() == null) {
                throw new RuntimeException("Product id is required");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }
        }
    }

    private UserResponse getUserById(UUID id) {
        BaseResponse<UserResponse> userResponse = userServiceClient.getUserById(id);
        if (userResponse == null || userResponse.getData() == null) {
            throw new RuntimeException("User not found");
        }
        return userResponse.getData();
    }

    private List<ProductResponse> fetchProducts(List<OrderItemRequest> items) {
        List<String> productIds = items.stream()
                .map(OrderItemRequest::getProductId)
                .toList();

        List<ProductResponse> products = productServiceClient.getProductsByIds(productIds).getData();
        if (CollectionUtils.isEmpty(products)) {
            throw new RuntimeException("Products not found");
        }
        return products;
    }

    private List<OrderItem> mapToOrderItems(Order order, Map<String, ProductResponse> productMap, List<OrderItemRequest> items) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest item : items) {
            ProductResponse product = productMap.get(item.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }
            orderItems.add(toOrderItem(order, product, item));
        }
        return orderItems;
    }


    private OrderItem toOrderItem(Order order, ProductResponse product, OrderItemRequest item) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setQuantity(item.getQuantity());
        orderItem.setPrice(product.getPrice());

        return orderItem;
    }
}
