package com.gnt.ecom.product.dto;

import com.gnt.ecom.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProductDTO {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

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
