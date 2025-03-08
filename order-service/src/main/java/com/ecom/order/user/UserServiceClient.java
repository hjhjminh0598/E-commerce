package com.ecom.order.user;

import com.ecom.order.base.BaseResponse;
import com.ecom.order.user.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{id}")
    BaseResponse<UserResponse> getUserById(@PathVariable UUID id);
}
