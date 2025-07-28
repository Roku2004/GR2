package com.example.userservice.service;

import com.example.userservice.Iservice.UserServiceI;
import com.example.userservice.model.Users;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceI {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            Users user = userOptional.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(authority));
        } else {
            throw new UsernameNotFoundException("Invalid email or password.");
        }
    }

    @Override
    public boolean registerUser(String name, String password, String email, String role, int gender, String avatar) {
        return false;
    }
}
