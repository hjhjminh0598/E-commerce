package com.gnt.ecom.user_authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    private String refreshToken;

    private String ipAddress;
}
