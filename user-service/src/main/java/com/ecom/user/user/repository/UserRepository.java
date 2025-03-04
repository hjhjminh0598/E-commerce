package com.ecom.user.user.repository;

import com.ecom.user.base.BaseRepository;
import com.ecom.user.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

}
