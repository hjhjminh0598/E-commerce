package com.ecom.user.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T, ID extends UUID> extends JpaRepository<T, ID> {

    @Query(value = "SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL",
            countQuery = "SELECT COUNT(e) FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    Page<T> findAllNotDeleted(Pageable pageable);

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1 AND e.deletedAt IS NULL")
    Optional<T> findByIdNotDeleted(ID id);

    @Query("SELECT e FROM #{#entityName} e WHERE e.id IN ?1 AND e.deletedAt IS NULL")
    List<T> findAllByIdNotDeleted(List<ID> ids);
}
