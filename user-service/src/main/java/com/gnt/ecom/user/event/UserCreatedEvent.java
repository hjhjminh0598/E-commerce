package com.gnt.ecom.user.event;

import com.gnt.ecom.user.entity.User;
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
