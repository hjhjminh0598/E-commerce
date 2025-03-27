package com.gnt.ecom.payment.controller;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.payment.dto.PaymentDTO;
import com.gnt.ecom.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService orderService;

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<PaymentDTO>>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getAllPayments(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PaymentDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<BaseResponse<PaymentDTO>> getByOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getByOrder(orderId)));
    }
}
