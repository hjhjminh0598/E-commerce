package com.ecom.product.product.repository;

import com.ecom.product.base.BaseRepository;
import com.ecom.product.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends BaseRepository<Product, UUID> {

}
