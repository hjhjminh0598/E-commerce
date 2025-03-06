package com.ecom.user.product;

import com.ecom.user.base.BaseResponse;
import com.ecom.user.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductServiceClient {

    @PostMapping("/products/batch")
    BaseResponse<List<ProductResponse>> getProductsByIds(@RequestBody List<String> productIds);
}
