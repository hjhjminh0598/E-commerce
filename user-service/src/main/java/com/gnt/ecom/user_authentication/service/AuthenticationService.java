package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;
import com.gnt.ecom.user_authentication.dto.RefreshTokenRequest;

public interface AuthenticationService {

    JwtResponse authenticate(LoginRequest request);

    JwtResponse refreshToken(RefreshTokenRequest request);
}
