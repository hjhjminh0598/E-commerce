package com.ecom.user.user.dto;

import com.ecom.user.user.entity.User;
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

    private LocalDateTime updatedAt;

    public static UserDTO of(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
}
