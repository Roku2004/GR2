package com.example.userservice.model;

import com.example.userservice.Enum.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleid", nullable = false)
    private Roles role;

    private Gender gender;

    @Column(name = "avatar", nullable = true)
    private String avatar;
}
