package com.gnt.ecom.user_authentication.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtResponse {

    private String token;

    private String refreshToken;

    public static JwtResponse of(String token, String refreshToken) {
        JwtResponse response = new JwtResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }
}
