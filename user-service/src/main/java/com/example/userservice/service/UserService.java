package com.example.userservice.service;

import com.example.userservice.Enum.Gender;
import com.example.userservice.model.Roles;
import com.example.userservice.model.Users;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Users> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            Users user = userOptional.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(authority));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    public boolean registerUser(String name, String password, String username, int role, int gender, String avatar) {
       Optional<Users> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return false;
        }

        Roles roles = roleRepository.findByRoleid(role);
        Gender gender1 = Gender.fromValue(gender);

        Users user = Users.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .username(username)
                .role(roles)
                .gender(gender1)
                .avatar(avatar)
                .build();
        userRepository.save(user);
        return true;
    }

    public boolean updatePassword(String username, String newPassword) {
        Optional<Users> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
