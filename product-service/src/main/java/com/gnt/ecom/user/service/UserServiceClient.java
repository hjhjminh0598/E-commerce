package com.gnt.ecom.user.service;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.user.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}")
    BaseResponse<UserResponse> getUserById(@PathVariable UUID id);
}
