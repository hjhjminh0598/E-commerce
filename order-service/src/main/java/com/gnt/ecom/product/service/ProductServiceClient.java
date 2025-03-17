package com.gnt.ecom.product.service;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @PostMapping("/api/v1/products/batch")
    BaseResponse<List<ProductResponse>> getProductsByIdsResponse(@RequestBody List<String> productIds);
}

