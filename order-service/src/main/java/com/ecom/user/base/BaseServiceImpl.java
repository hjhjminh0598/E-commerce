package com.ecom.user.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseServiceImpl<T extends BaseEntity, ID extends UUID> implements BaseService<T, ID> {

    protected final BaseRepository<T, ID> repository;

    protected BaseServiceImpl(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAllNotDeleted(pageable);
    }

    public Optional<T> findById(ID id) {
        return repository.findByIdNotDeleted(id);
    }

    public List<T> findAllById(List<ID> ids) {
        return repository.findAllByIdNotDeleted(ids);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public boolean delete(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isEmpty()) {
            return false;
        }

        entity.get().softDelete();
        repository.save(entity.get());
        return true;
    }
}
