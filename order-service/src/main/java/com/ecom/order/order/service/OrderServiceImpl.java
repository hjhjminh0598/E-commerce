package com.ecom.order.order.service;

import com.ecom.order.base.BaseRepository;
import com.ecom.order.base.BaseResponse;
import com.ecom.order.base.BaseServiceImpl;
import com.ecom.order.base.PageResponse;
import com.ecom.order.exchange_rate.service.ExchangeRateService;
import com.ecom.order.order.dto.CreateOrderRequest;
import com.ecom.order.order.dto.OrderDTO;
import com.ecom.order.order.dto.OrderItemDTO;
import com.ecom.order.order.dto.OrderItemRequest;
import com.ecom.order.order.entity.Order;
import com.ecom.order.order.entity.OrderItem;
import com.ecom.order.order.repository.OrderRepository;
import com.ecom.order.product.ProductServiceClient;
import com.ecom.order.product.dto.ProductResponse;
import com.ecom.order.user.UserServiceClient;
import com.ecom.order.user.dto.UserResponse;
import com.ecom.order.utils.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, UUID> implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemService orderItemService;

    private final UserServiceClient userServiceClient;

    private final ProductServiceClient productServiceClient;

    private final ExchangeRateService exchangeRateService;

    protected OrderServiceImpl(BaseRepository<Order, UUID> repository, OrderRepository orderRepository, OrderItemService orderItemService, ProductServiceClient productServiceClient, UserServiceClient userServiceClient, ExchangeRateService exchangeRateService) {
        super(repository);
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.productServiceClient = productServiceClient;
        this.userServiceClient = userServiceClient;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public PageResponse<OrderDTO> getAllOrders(Pageable pageable) {
        return PageResponse.from(super.findAll(pageable).map(this::toOrderDTO));
    }

    @Override
    public PageResponse<OrderDTO> getByUser(UUID userId, Pageable pageable) {
        return PageResponse.from(orderRepository.findAllByUserId(userId, pageable).map(this::toOrderDTO));
    }

    @Override
    public OrderDTO getById(UUID id) {
        return super.findById(id).map(this::toOrderDTO).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO create(CreateOrderRequest request) {
        validateOrderRequest(request);
        UserResponse user = getUserById(request.getUserId());

        List<ProductResponse> products = fetchProducts(request.getItems());
        Map<String, ProductResponse> productMap = products.stream()
                .collect(Collectors.toMap(p -> p.getId().toString(), Function.identity()));

        Order order = new Order();
        order.setUserId(user.getId());
        order.setUserCurrency(user.getCurrency());

        List<OrderItem> items = mapToOrderItems(order, productMap, request.getItems());
        setPrice(order, items);
        order = orderRepository.save(order);
        items = orderItemService.saveAll(items);

        return OrderDTO.of(order, items.stream().map(OrderItemDTO::of).toList());
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

        List<ProductResponse> products = productServiceClient.getProductByIds(productIds);
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

    private OrderDTO toOrderDTO(Order order) {
        return OrderDTO.of(order, orderItemService.getByOrderId(order.getId()));
    }
}
