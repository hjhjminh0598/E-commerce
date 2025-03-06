package com.ecom.user.order.controller;

import com.ecom.user.base.BaseResponse;
import com.ecom.user.base.PageResponse;
import com.ecom.user.order.dto.CreateOrderRequest;
import com.ecom.user.order.dto.OrderDTO;
import com.ecom.user.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<OrderDTO>>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getAllOrders(pageable)));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<OrderDTO>> create(@RequestBody CreateOrderRequest request) {
        try {
            return ResponseEntity.ok(BaseResponse.success(orderService.create(request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }
}
