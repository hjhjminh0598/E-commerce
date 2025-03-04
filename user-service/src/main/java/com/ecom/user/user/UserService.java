package com.ecom.user.user;

import com.ecom.user.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService extends BaseService<User, UUID> {

    public UserService(UserRepository repository) {
        super(repository);
    }
}
