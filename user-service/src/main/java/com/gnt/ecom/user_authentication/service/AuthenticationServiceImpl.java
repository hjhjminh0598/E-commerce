package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.user.service.UserService;
import com.gnt.ecom.user_authentication.config.JwtProvider;
import com.gnt.ecom.user_authentication.config.SecurityUtils;
import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;
import com.gnt.ecom.user_authentication.dto.OAuth2Response;
import com.gnt.ecom.user_authentication.dto.RefreshTokenRequest;
import com.gnt.ecom.user_authentication.entity.MyUserDetails;
import com.gnt.ecom.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Override
    public JwtResponse authenticate(LoginRequest request) {
        validateRequest(request);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        MyUserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String accessToken = jwtProvider.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername(), request.getIpAddress());

        return JwtResponse.of(accessToken, refreshToken);
    }

    private void validateRequest(LoginRequest request) {
        if (StringUtils.isBlank(request.getUsername())) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (request.getUsername().length() < 4) {
            throw new IllegalArgumentException("Username must be at least 4 characters long");
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        request.trim();
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String username = refreshTokenService.validateRefreshToken(request.getRefreshToken(), request.getIpAddress());
        MyUserDetails userDetails = userService.loadUserByUsername(username);

        String newAccessToken = jwtProvider.generateToken(userDetails);
        String newRefreshToken = refreshTokenService.createRefreshToken(username, request.getIpAddress());
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());

        return JwtResponse.of(newAccessToken, newRefreshToken);
    }

    @Override
    public OAuth2Response loginWithGoogle(OAuth2AuthenticationToken authentication) {
        OAuth2User oauth2User = authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String provider = authentication.getAuthorizedClientRegistrationId();

        String token = jwtProvider.generateToken(email, provider);
        return OAuth2Response.builder()
                .email(email)
                .token(token)
                .provider(provider)
                .build();
    }

    @Override
    public int logout() {
        return refreshTokenService.revokeAllUserTokens(SecurityUtils.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not logged in")));
    }
}