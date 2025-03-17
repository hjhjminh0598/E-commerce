package com.gnt.ecom.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    private String name;

    private String description;

    private Double price;
}
