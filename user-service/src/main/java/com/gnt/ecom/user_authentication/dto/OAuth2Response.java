package com.gnt.ecom.user_authentication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OAuth2Response {

    private String email;

    private String token;

    private String provider;
}
