package com.gnt.ecom.product.repository;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends BaseRepository<Product, UUID> {

}
