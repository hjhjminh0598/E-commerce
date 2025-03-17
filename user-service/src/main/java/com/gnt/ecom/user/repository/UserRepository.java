package com.gnt.ecom.user.repository;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

}
