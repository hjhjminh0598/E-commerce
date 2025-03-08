package com.ecom.product.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {

    Page<T> findAll(Pageable pageable);

    Optional<T> findById(ID id);

    List<T> findAllById(List<ID> ids);

    T save(T entity);

    List<T> saveAll(Collection<T> entities);

    boolean delete(ID id);
}
