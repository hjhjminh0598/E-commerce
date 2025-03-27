package com.gnt.ecom.order;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.order.service.OrderStreamService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class OrderStreamController {

    private static final Logger log = LoggerFactory.getLogger(OrderStreamController.class);
    private final OrderStreamService orderStreamService;

    @GetMapping("/order-streams/sales-by-currency")
    public ResponseEntity<BaseResponse<Map<String, Double>>> getSalesByCurrency(@RequestParam(required = false) String[] currencies) {
        try {
            return ResponseEntity.ok(BaseResponse.success(orderStreamService.getSalesByCurrency(currencies)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }
}