package com.ecom.user.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String username;

    private String email;
}
