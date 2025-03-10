package com.ecom.order.product.service;

import com.ecom.order.product.dto.ProductResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductServiceClient productServiceClient;

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetProductsByIds")
    @Retry(name = "productService")
    @Bulkhead(name = "productService", fallbackMethod = "fallbackBulkheadGetProductsByIds")
    @RateLimiter(name = "productService", fallbackMethod = "fallbackRateLimiterGetProductsByIds")
    public List<ProductResponse> getProductsByIds(List<String> productIds) {
        log.info("Fetching products with IDs: {}", productIds);
        return productServiceClient.getProductsByIdsResponse(productIds).getData();
    }

    public List<ProductResponse> fallbackGetProductsByIds(List<String> productIds, Throwable t) {
        log.warn("Fallback executed due to circuit breaker or retry failure.");
        return Collections.emptyList();
    }

    public List<ProductResponse> fallbackBulkheadGetProductsByIds(List<String> productIds, Throwable t) {
        log.warn("Fallback executed due to Bulkhead limit reached.");
        return Collections.emptyList();
    }

    public List<ProductResponse> fallbackRateLimiterGetProductsByIds(List<String> productIds, Throwable t) {
        log.warn("Fallback executed due to rate limit reached.");
        return Collections.emptyList();
    }
}

