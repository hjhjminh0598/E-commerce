package com.ecom.user.base;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseService<T extends BaseEntity, ID extends UUID> {

    protected final BaseRepository<T, ID> repository;

    protected BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAllNotDeleted();
    }

    public Optional<T> findById(ID id) {
        return repository.findByIdNotDeleted(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void softDelete(ID id) {
        Optional<T> entity = repository.findById(id);
        entity.ifPresent(e -> {
            e.softDelete();
            repository.save(e);
        });
    }
}
