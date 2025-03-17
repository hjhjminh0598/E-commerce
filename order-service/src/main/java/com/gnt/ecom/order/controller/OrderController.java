package com.gnt.ecom.order.controller;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.order.dto.CreateOrderRequest;
import com.gnt.ecom.order.dto.OrderDTO;
import com.gnt.ecom.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<PageResponse<OrderDTO>>> getByUser(@PathVariable UUID userId,
                                                                          @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getByUser(userId, pageable)));
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
