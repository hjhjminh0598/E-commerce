package com.gnt.ecom.order.service;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.base.BaseServiceImpl;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.exchange_rate.service.ExchangeRateService;
import com.gnt.ecom.order.dto.CreateOrderRequest;
import com.gnt.ecom.order.dto.OrderDTO;
import com.gnt.ecom.order.dto.OrderItemDTO;
import com.gnt.ecom.order.dto.OrderItemRequest;
import com.gnt.ecom.order.entity.Order;
import com.gnt.ecom.order.entity.OrderItem;
import com.gnt.ecom.order.event.OrderCreatedEvent;
import com.gnt.ecom.order.producer.OrderProducer;
import com.gnt.ecom.order.repository.OrderRepository;
import com.gnt.ecom.product.service.ProductService;
import com.gnt.ecom.product.dto.ProductResponse;
import com.gnt.ecom.user.UserServiceClient;
import com.gnt.ecom.user.dto.UserResponse;
import com.gnt.ecom.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, UUID> implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemService orderItemService;

    private final UserServiceClient userServiceClient;

    private final ProductService productService;

    private final ExchangeRateService exchangeRateService;

    private final OrderProducer orderProducer;

    protected OrderServiceImpl(BaseRepository<Order, UUID> repository, OrderRepository orderRepository, OrderItemService orderItemService, UserServiceClient userServiceClient, ProductService productService, ExchangeRateService exchangeRateService, OrderProducer orderProducer) {
        super(repository);
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.userServiceClient = userServiceClient;
        this.exchangeRateService = exchangeRateService;
        this.orderProducer = orderProducer;
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
        order.setMethod(request.getPaymentMethod());
        order.setUserCurrency(user.getCurrency());

        List<OrderItem> items = mapToOrderItems(order, productMap, request.getItems());
        setPrice(order, items);
        order = orderRepository.save(order);
        items = orderItemService.saveAll(items);

        orderProducer.publishOrderCreatedEvent(OrderCreatedEvent.of(order));

        return OrderDTO.of(order, items.stream().map(OrderItemDTO::of).toList());
    }

    private void setPrice(Order order, List<OrderItem> items) {
        BigDecimal totalPrice = items.stream()
                .filter(i -> i.getPrice().compareTo(BigDecimal.ZERO) >= 0 && i.getQuantity() > 0)
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        order.setTotalLocalPrice(totalPrice.multiply(
                BigDecimal.valueOf(exchangeRateService.getExchangeRateBaseUSD(order.getUserCurrency()))));
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

        List<ProductResponse> products = productService.getProductsByIds(productIds);
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
