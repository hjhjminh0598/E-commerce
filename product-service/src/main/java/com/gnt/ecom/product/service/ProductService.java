package com.gnt.ecom.product.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.product.dto.CreateProductRequest;
import com.gnt.ecom.product.dto.ProductDTO;
import com.gnt.ecom.product.dto.UpdateProductRequest;
import com.gnt.ecom.product.entity.Product;
import com.gnt.ecom.user.dto.UserResponse;
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
