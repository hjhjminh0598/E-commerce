package com.ecom.user.user.event;

import com.ecom.user.user.entity.User;
import lombok.Data;

@Data
public class UserCreatedEvent {

    private String id;

    private String username;

    private String email;

    public static UserCreatedEvent of(User user) {
        UserCreatedEvent event = new UserCreatedEvent();
        event.setId(user.getId().toString());
        event.setUsername(user.getUsername());
        event.setEmail(user.getEmail());
        return event;
    }
}
