package com.ecom.user.user.service;

import com.ecom.user.base.BaseService;
import com.ecom.user.base.PageResponse;
import com.ecom.user.user.dto.CreateUserRequest;
import com.ecom.user.user.dto.UpdateUserRequest;
import com.ecom.user.user.dto.UserDTO;
import com.ecom.user.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService extends BaseService<User, UUID> {

    PageResponse<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getById(UUID id);

    UserDTO create(CreateUserRequest request);

    UserDTO update(UUID id, UpdateUserRequest request);
}
