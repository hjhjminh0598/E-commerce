package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;
import com.gnt.ecom.user_authentication.dto.OAuth2Response;
import com.gnt.ecom.user_authentication.dto.RefreshTokenRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface AuthenticationService {

    JwtResponse authenticate(LoginRequest request);

    JwtResponse refreshToken(RefreshTokenRequest request);

    OAuth2Response loginWithGoogle(OAuth2AuthenticationToken authentication);

    int logout();
}
