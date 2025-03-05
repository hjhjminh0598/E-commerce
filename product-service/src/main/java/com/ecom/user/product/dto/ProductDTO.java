package com.ecom.user.product.dto;

import com.ecom.user.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProductDTO {

    private UUID id;

    private String name;

    private String description;

    private Double price;

    private LocalDateTime updatedAt;

    public static ProductDTO of(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        return productDTO;
    }
}
