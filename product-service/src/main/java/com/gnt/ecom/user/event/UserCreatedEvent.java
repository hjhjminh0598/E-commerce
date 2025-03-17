package com.gnt.ecom.user.event;

import lombok.Data;

@Data
public class UserCreatedEvent {

    private String id;

    private String username;

    private String email;
}
