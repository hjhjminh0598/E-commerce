package com.ecom.product.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    private String name;

    private String description;

    private Double price;
}
