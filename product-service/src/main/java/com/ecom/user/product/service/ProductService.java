package com.ecom.user.product.service;

import com.ecom.user.base.BaseService;
import com.ecom.user.product.dto.CreateProductRequest;
import com.ecom.user.product.dto.ProductDTO;
import com.ecom.user.product.dto.UpdateProductRequest;
import com.ecom.user.product.entity.Product;
import com.ecom.user.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService extends BaseService<Product, UUID> {

    List<ProductDTO> getAllProducts();

    ProductDTO getById(UUID id);

    ProductDTO create(CreateProductRequest request);

    ProductDTO update(UUID id, UpdateProductRequest request);

    UserResponse getUserById(UUID id);
}
