package com.ecom.user.user.controller;

import com.ecom.user.base.BaseResponse;
import com.ecom.user.user.dto.CreateUserDTO;
import com.ecom.user.user.dto.UpdateUserDTO;
import com.ecom.user.user.service.UserService;
import com.ecom.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<BaseResponse<List<User>>> getAll() {
        return ResponseEntity.ok(BaseResponse.success(userService.findAll()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<BaseResponse<User>> getById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.findById(UUID.fromString(id))
                    .map(BaseResponse::success)
                    .orElseGet(BaseResponse::failure));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure());
        }
    }

    @PostMapping("/user")
    public ResponseEntity<BaseResponse<User>> create(@RequestBody CreateUserDTO createUserDTO) {
        try {
            return ResponseEntity.ok(BaseResponse.success(userService.create(createUserDTO)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<BaseResponse<User>> update(@PathVariable String id,
                                                     @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            return ResponseEntity.ok(BaseResponse.success(userService.update(UUID.fromString(id), updateUserDTO)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable String id) {
        try {
            if (userService.softDelete(UUID.fromString(id))) {
                return ResponseEntity.ok(BaseResponse.successNoData());
            }

            return ResponseEntity.ok(BaseResponse.failure());
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure());
        }
    }
}
