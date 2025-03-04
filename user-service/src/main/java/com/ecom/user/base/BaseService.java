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

    public boolean softDelete(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isEmpty()) {
            return false;
        }

        entity.get().softDelete();
        repository.save(entity.get());
        return true;
    }
}
