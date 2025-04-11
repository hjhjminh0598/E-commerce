package com.gnt.ecom.user_authentication.controller;

import com.gnt.ecom.base.BaseResponse;
import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;
import com.gnt.ecom.user_authentication.dto.RefreshTokenRequest;
import com.gnt.ecom.user_authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@Controller
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtResponse>> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        try {
            request.setIpAddress(httpServletRequest.getRemoteAddr());
            return ResponseEntity.ok(BaseResponse.success(authenticationService.authenticate(request)));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<BaseResponse<JwtResponse>> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpServletRequest) {
        try {
            request.setIpAddress(httpServletRequest.getRemoteAddr());
            return ResponseEntity.ok(BaseResponse.success(authenticationService.refreshToken(request)));
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

    @GetMapping("/login/google")
    public ResponseEntity<Void> loginWithGoogle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:8080/oauth2/authorization/google"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 redirect
    }
}
