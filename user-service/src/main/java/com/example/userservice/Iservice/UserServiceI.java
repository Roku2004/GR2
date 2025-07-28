package com.example.userservice.Iservice;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserServiceI {
    UserDetails loadUserByUsername(String email);
    boolean registerUser(String name, String password, String email, String role, int gender, String avatar);
}
