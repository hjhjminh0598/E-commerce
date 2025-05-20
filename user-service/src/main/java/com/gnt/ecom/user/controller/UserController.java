package com.gnt.ecom.user.controller;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.user.dto.CreateUserRequest;
import com.gnt.ecom.user.dto.UpdateUserRequest;
import com.gnt.ecom.user.dto.UserDTO;
import com.gnt.ecom.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse<PageResponse<UserDTO>>> getAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(BaseResponse.success(userService.getAllUsers(pageable)));
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
