package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.user_authentication.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService extends BaseService<RefreshToken, UUID> {

    String createRefreshToken(String username);

    String validateRefreshToken(String token);

    void deleteRefreshToken(String token);
}
