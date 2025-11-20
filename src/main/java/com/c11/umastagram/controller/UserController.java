/*
    Author: Armando Vega
    Date Created: 18 November 2025
    Last Modified By: Armando Vega
    Date Last Modified: 18 November 2025
    Description: UserController class to handle user-related endpoints.
*/
package com.c11.umastagram.controller;

import com.c11.umastagram.dto.SignupRequest;
import com.c11.umastagram.model.User;
import com.c11.umastagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    // Define user-related endpoints here
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            // Convert DTO to User entity
            User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword()
            );
            
            User savedUser = userService.saveUser(user);
            
            // Return only safe fields (no password)
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "userId", savedUser.getUserId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try{
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
            }

            // Implement login logic here (e.g., verify credentials, generate token, etc.)
            
            User user = userService.findUserByUsername(username).get();
            if (user == null || !user.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
            }

            return ResponseEntity.ok(Map.of("message", "Login successful for user: " + username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred during login"));
        }
    }
}