package com.ecom.order.product;

import com.ecom.order.base.BaseResponse;
import com.ecom.order.product.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @PostMapping("/api/v1/products/batch")
    BaseResponse<List<ProductResponse>> getProductsByIdsResponse(@RequestBody List<String> productIds);

    default List<ProductResponse> getProductByIds(List<String> productIds) {
        try {
            BaseResponse<List<ProductResponse>> productsResponse = getProductsByIdsResponse(productIds);
            return productsResponse == null ? List.of() : productsResponse.getData();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
