package com.ecom.user.user.controller;

import com.ecom.user.base.BaseResponse;
import com.ecom.user.user.dto.CreateUserRequest;
import com.ecom.user.user.dto.UpdateUserRequest;
import com.ecom.user.user.dto.UserDTO;
import com.ecom.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAll() {
        return ResponseEntity.ok(BaseResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(BaseResponse.success(userService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure());
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<UserDTO>> create(@RequestBody CreateUserRequest request) {
        try {
            return ResponseEntity.ok(BaseResponse.success(userService.create(request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> update(@PathVariable UUID id,
                                                        @RequestBody UpdateUserRequest request) {
        try {
            return ResponseEntity.ok(BaseResponse.success(userService.update(id, request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable UUID id) {
        try {
            if (userService.delete(id)) {
                return ResponseEntity.ok(BaseResponse.successNoData());
            }

            return ResponseEntity.ok(BaseResponse.failure());
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure());
        }
    }
}
