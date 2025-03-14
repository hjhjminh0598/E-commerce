package com.ecom.order.user.event;

import lombok.Data;

@Data
public class UserCreatedEvent {

    private String id;

    private String username;

    private String email;
}
