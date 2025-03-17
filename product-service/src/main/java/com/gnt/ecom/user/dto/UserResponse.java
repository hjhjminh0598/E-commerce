package com.gnt.ecom.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private String currency;

    private LocalDateTime updatedAt;
}
