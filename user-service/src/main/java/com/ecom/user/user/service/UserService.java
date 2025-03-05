package com.ecom.user.user.service;

import com.ecom.user.base.BaseService;
import com.ecom.user.user.dto.CreateUserRequest;
import com.ecom.user.user.dto.UpdateUserRequest;
import com.ecom.user.user.dto.UserDTO;
import com.ecom.user.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseService<User, UUID> {

    List<UserDTO> getAllUsers();

    UserDTO getById(UUID id);

    UserDTO create(CreateUserRequest request);

    UserDTO update(UUID id, UpdateUserRequest request);
}
