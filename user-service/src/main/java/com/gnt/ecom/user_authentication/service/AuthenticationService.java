package com.gnt.ecom.user_authentication.service;

import com.gnt.ecom.user_authentication.dto.JwtResponse;
import com.gnt.ecom.user_authentication.dto.LoginRequest;

public interface AuthenticationService {

    JwtResponse authenticate(LoginRequest request);
}
