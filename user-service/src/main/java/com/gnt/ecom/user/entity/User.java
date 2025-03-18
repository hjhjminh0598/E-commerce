package com.gnt.ecom.user.entity;

import com.gnt.ecom.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @Column(length = 3)
    private String currency;
}
