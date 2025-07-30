package com.example.userservice.controller;

import com.example.userservice.config.JwtService;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AccountController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest body) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.username, body.password));

            String username = authentication.getName();
            String token = jwtService.generateToken(username);

            Map<String, Object> response = Map.of(
                    "token", token,
                    "username", username,
                    "role", userRepository.findByUsername(username)
                            .orElseThrow(() -> new BadCredentialsException("User not found"))
                            .getRole().getRoleid(),
                    "message", "Login successful"
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest body) {
        try{
            userService.registerUser(body.name, body.password, body.username, body.role, body.gender, body.avatar);
            return ResponseEntity.ok("User registered successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody LoginRequest request) {
        try {
            boolean isUpdated = userService.updatePassword(request.username, request.password);
            if (isUpdated) {
                return ResponseEntity.ok("Password updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password");
        }
    }

}
