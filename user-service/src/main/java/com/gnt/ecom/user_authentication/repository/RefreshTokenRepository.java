package com.gnt.ecom.user_authentication.repository;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.user_authentication.entity.RefreshToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, UUID> {

    @Query(value = "SELECT r FROM RefreshToken r WHERE r.token = ?1 AND r.deletedAt IS NULL")
    Optional<RefreshToken> findByToken(String token);
}
