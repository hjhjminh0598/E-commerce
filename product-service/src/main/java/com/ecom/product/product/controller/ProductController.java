package com.ecom.product.product.controller;

import com.ecom.product.base.BaseResponse;
import com.ecom.product.base.PageResponse;
import com.ecom.product.product.dto.CreateProductRequest;
import com.ecom.product.product.dto.ProductDTO;
import com.ecom.product.product.dto.UpdateProductRequest;
import com.ecom.product.product.service.ProductService;
import com.ecom.product.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<ProductDTO>>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(productService.getAllProducts(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(BaseResponse.success(productService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<BaseResponse<List<ProductDTO>>> getByIds(@RequestBody List<String> productIds) {
        try {
            return ResponseEntity.ok(BaseResponse.success(productService.getAllByIds(productIds)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ProductDTO>> create(@RequestBody CreateProductRequest request) {
        try {
            return ResponseEntity.ok(BaseResponse.success(productService.create(request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> update(@PathVariable UUID id,
                                                           @RequestBody UpdateProductRequest request) {
        try {
            return ResponseEntity.ok(BaseResponse.success(productService.update(id, request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable UUID id) {
        try {
            if (productService.delete(id)) {
                return ResponseEntity.ok(BaseResponse.successNoData());
            }

            return ResponseEntity.ok(BaseResponse.failure());
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> getUsers(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(productService.getUserById(id)));
    }
}
