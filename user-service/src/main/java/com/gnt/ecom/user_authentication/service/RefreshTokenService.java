package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.user.entity.User;
import com.gnt.ecom.user_authentication.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService extends BaseService<RefreshToken, UUID> {

    String createRefreshToken(String username, String ipAddress);

    String validateRefreshToken(String token, String ipAddress);

    void deleteRefreshToken(String token);

    int revokeAllUserTokens(User user);
}
