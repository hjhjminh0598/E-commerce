package com.ecom.user.user;

import com.ecom.user.base.BaseResponse;
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
    public ResponseEntity<BaseResponse<User>> create(@RequestBody User entity) {
        return ResponseEntity.ok(BaseResponse.success(userService.save(entity)));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable String id) {
        userService.softDelete(UUID.fromString(id));
        return ResponseEntity.ok(BaseResponse.successNoData());
    }
}
