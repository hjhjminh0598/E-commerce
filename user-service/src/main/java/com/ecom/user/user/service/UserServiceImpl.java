package com.ecom.user.user.service;

import com.ecom.user.base.BaseServiceImpl;
import com.ecom.user.base.PageResponse;
import com.ecom.user.user.dto.CreateUserRequest;
import com.ecom.user.user.dto.UpdateUserRequest;
import com.ecom.user.user.dto.UserDTO;
import com.ecom.user.user.entity.User;
import com.ecom.user.user.event.UserCreatedEvent;
import com.ecom.user.user.producer.UserProducer;
import com.ecom.user.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UUID> implements UserService {

    private final UserProducer userProducer;

    public UserServiceImpl(UserRepository repository, UserProducer userProducer) {
        super(repository);
        this.userProducer = userProducer;
    }

    @Override
    public PageResponse<UserDTO> getAllUsers(Pageable pageable) {
        return PageResponse.from(super.findAll(pageable).map(UserDTO::of));
    }

    @Override
    public UserDTO getById(UUID id) {
        return super.findById(id).map(UserDTO::of).orElse(null);
    }

    public UserDTO create(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user = super.save(user);
        userProducer.publishUserCreatedEvent(UserCreatedEvent.of(user));

        return UserDTO.of(user);
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
