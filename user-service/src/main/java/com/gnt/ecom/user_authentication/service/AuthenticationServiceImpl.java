package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.user.service.UserService;
import com.gnt.ecom.user_authentication.config.JwtProvider;
import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;
import com.gnt.ecom.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtProvider jwtUtil;

    @Override
    public JwtResponse authenticate(LoginRequest request) {
        validateRequest(request);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

        return JwtResponse.of(jwtUtil.generateToken(userDetails));
    }

    private void validateRequest(LoginRequest request) {
        if (StringUtils.isBlank(request.getUsername())) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (request.getUsername().length() < 4) {
            throw new IllegalArgumentException("Username must be at least 4 characters long");
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        request.trim();
    }
}