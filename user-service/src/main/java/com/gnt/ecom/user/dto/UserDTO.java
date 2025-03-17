package com.gnt.ecom.user.dto;

import com.gnt.ecom.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private UUID id;

    private String username;

    private String email;

    private String phoneNumber;

    private String currency;

    private LocalDateTime updatedAt;

    public static UserDTO of(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setCurrency(user.getCurrency());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
}
