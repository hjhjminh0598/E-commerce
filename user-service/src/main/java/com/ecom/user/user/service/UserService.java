package com.ecom.user.user.service;

import com.ecom.user.base.BaseService;
import com.ecom.user.user.dto.CreateUserDTO;
import com.ecom.user.user.dto.UpdateUserDTO;
import com.ecom.user.user.entity.User;
import com.ecom.user.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService extends BaseService<User, UUID> {

    public UserService(UserRepository repository) {
        super(repository);
    }

    public User create(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        return save(user);
    }

    public User update(UUID id, UpdateUserDTO updateUserDTO) {
        return findById(id)
                .map(user -> {
                    user.setPhoneNumber(updateUserDTO.getPhoneNumber());
                    return save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
