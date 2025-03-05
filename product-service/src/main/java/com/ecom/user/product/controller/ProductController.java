package com.ecom.user.product.controller;

import com.ecom.user.base.BaseResponse;
import com.ecom.user.product.dto.CreateProductRequest;
import com.ecom.user.product.dto.ProductDTO;
import com.ecom.user.product.dto.UpdateProductRequest;
import com.ecom.user.product.service.ProductService;
import com.ecom.user.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<BaseResponse<List<ProductDTO>>> getAll() {
        return ResponseEntity.ok(BaseResponse.success(productService.getAllProducts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductDTO>> getById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(BaseResponse.success(productService.getById(UUID.fromString(id))));
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
    public ResponseEntity<BaseResponse<ProductDTO>> update(@PathVariable String id,
                                                           @RequestBody UpdateProductRequest request) {
        try {
            return ResponseEntity.ok(BaseResponse.success(productService.update(UUID.fromString(id), request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable String id) {
        try {
            if (productService.delete(UUID.fromString(id))) {
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
