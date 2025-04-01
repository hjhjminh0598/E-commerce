package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.base.BaseServiceImpl;
import com.gnt.ecom.user.service.UserService;
import com.gnt.ecom.user_authentication.config.JwtProvider;
import com.gnt.ecom.user_authentication.entity.MyUserDetails;
import com.gnt.ecom.user_authentication.entity.RefreshToken;
import com.gnt.ecom.user_authentication.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenServiceImpl extends BaseServiceImpl<RefreshToken, UUID> implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserService userService;

    private final JwtProvider jwtProvider;

    protected RefreshTokenServiceImpl(RefreshTokenRepository repository, UserService userService, JwtProvider jwtProvider) {
        super(repository);
        this.refreshTokenRepository = repository;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public String createRefreshToken(String username) {
        MyUserDetails userDetails = userService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(jwtProvider.generateRefreshToken(userDetails));
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(TimeUnit.MICROSECONDS.toSeconds(refreshTokenExpiration)));
        refreshToken.setUser(userDetails.getUser());

        refreshToken = save(refreshToken);
        logger.info("Created refresh token for user: {}", username);
        return refreshToken.getToken();
    }

    @Override
    @Transactional(readOnly = true)
    public String validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            delete(refreshToken.getId());
            throw new IllegalArgumentException("Refresh token expired");
        }

        return refreshToken.getUser().getUsername();
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            delete(refreshToken.getId());
            logger.info("Deleted refresh token for user: {}", refreshToken.getUser().getUsername());
        });
    }
}
