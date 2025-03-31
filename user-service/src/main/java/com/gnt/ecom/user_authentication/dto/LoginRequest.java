package com.gnt.ecom.user_authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String username;

    private String password;

    public void trim() {
        username = username.trim();
        password = password.trim();
    }
}
