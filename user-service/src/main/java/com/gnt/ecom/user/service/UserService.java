package com.gnt.ecom.user.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.user.dto.CreateUserRequest;
import com.gnt.ecom.user.dto.UpdateUserRequest;
import com.gnt.ecom.user.dto.UserDTO;
import com.gnt.ecom.user.entity.User;
import com.gnt.ecom.user_authentication.dto.OAuth2Response;
import com.gnt.ecom.user_authentication.entity.MyUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.UUID;

public interface UserService extends BaseService<User, UUID>, UserDetailsService {

    MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User getByUsername(String username);

    PageResponse<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getById(UUID id);

    UserDTO create(CreateUserRequest request);

    UserDTO update(UUID id, UpdateUserRequest request);

    OAuth2Response loginWithGoogle(OAuth2AuthenticationToken authentication);
}
