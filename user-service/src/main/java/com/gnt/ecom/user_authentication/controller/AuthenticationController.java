package com.gnt.ecom.user_authentication.controller;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;
import com.gnt.ecom.user_authentication.dto.RefreshTokenRequest;
import com.gnt.ecom.user_authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtResponse>> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(BaseResponse.success(authenticationService.authenticate(request, httpServletRequest.getRemoteAddr())));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<BaseResponse<JwtResponse>> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(BaseResponse.success(authenticationService.refreshToken(request, httpServletRequest.getRemoteAddr())));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Integer>> logout() {
        try {
            return ResponseEntity.ok(BaseResponse.success(authenticationService.logout()));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e));
        }
    }
}
