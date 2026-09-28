package com.nvrsocial.market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity

@Getter
@Setter

@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String password;
    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User () {}

    public User(Long id, String email, String username,
                String password, LocalDateTime createAd, Role role) {

        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createAt = createAd;
        this.role = role;
    }

}
