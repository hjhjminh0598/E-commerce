package com.ecom.user.product.entity;

import com.ecom.user.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;

    private String description;

    private Double price;
}
