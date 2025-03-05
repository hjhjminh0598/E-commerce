package com.ecom.user.user.service;

import com.ecom.user.base.BaseServiceImpl;
import com.ecom.user.user.dto.CreateUserRequest;
import com.ecom.user.user.dto.UpdateUserRequest;
import com.ecom.user.user.dto.UserDTO;
import com.ecom.user.user.entity.User;
import com.ecom.user.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UUID> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return super.findAll().stream().map(UserDTO::of).toList();
    }

    @Override
    public UserDTO getById(UUID id) {
        return super.findById(id).map(UserDTO::of).orElse(null);
    }

    public UserDTO create(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return UserDTO.of(super.save(user));
    }

    public UserDTO update(UUID id, UpdateUserRequest request) {
        return UserDTO.of(super.findById(id)
                .map(user -> {
                    user.setPhoneNumber(request.getPhoneNumber());
                    return save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
}
