package com.gnt.ecom.user.service;

import com.gnt.ecom.user.dto.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final static UserResponse DEFAULT_USER = new UserResponse();

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserById")
    public UserResponse getUserById(UUID id) {
        return userServiceClient.getUserById(id).getData();
    }

    private UserResponse fallbackGetUserById(UUID id, Throwable throwable) {
        log.warn("Circuit Breaker triggered while fetching user: {}", id);
        return DEFAULT_USER;
    }
}
