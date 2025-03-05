package com.ecom.user.product.repository;

import com.ecom.user.base.BaseRepository;
import com.ecom.user.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends BaseRepository<Product, UUID> {


}
