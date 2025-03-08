package com.ecom.product.product.service;

import com.ecom.product.base.BaseService;
import com.ecom.product.base.PageResponse;
import com.ecom.product.product.dto.CreateProductRequest;
import com.ecom.product.product.dto.ProductDTO;
import com.ecom.product.product.dto.UpdateProductRequest;
import com.ecom.product.product.entity.Product;
import com.ecom.product.user.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService extends BaseService<Product, UUID> {

    PageResponse<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO getById(UUID id);

    List<ProductDTO> getAllByIds(List<String> ids);

    ProductDTO create(CreateProductRequest request);

    ProductDTO update(UUID id, UpdateProductRequest request);

    UserResponse getUserById(UUID id);
}
