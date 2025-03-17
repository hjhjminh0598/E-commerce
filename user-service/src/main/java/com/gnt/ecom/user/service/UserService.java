package com.gnt.ecom.user.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.user.dto.CreateUserRequest;
import com.gnt.ecom.user.dto.UpdateUserRequest;
import com.gnt.ecom.user.dto.UserDTO;
import com.gnt.ecom.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService extends BaseService<User, UUID> {

    PageResponse<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getById(UUID id);

    UserDTO create(CreateUserRequest request);

    UserDTO update(UUID id, UpdateUserRequest request);
}
