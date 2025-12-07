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
import com.c11.umastagram.dto.LoginRequest;

import java.util.List;
import java.util.Optional;
import com.c11.umastagram.util.JwtUtil;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try{
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
    
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
            }

            // Implement login logic here (e.g., verify credentials, generate token, etc.)
            
            User user = userService.findUserByUsername(username).get();
            if (user == null || !user.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
            }
            Long userId = user.getUserId();
            String email = user.getEmail();
            String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());
            return ResponseEntity.ok(Map.of(
                "message", "Login successful for user: " + username,
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "token", token
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred during login"));
        }
    }

    @PostMapping("/username/change")
    public ResponseEntity<?> changeUsername(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newUsername = request.get("newUsername");

            if (token == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
            }
            if (token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token cannot be empty"));
            }
            if (newUsername == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "New username is required"));
            }
            if (newUsername.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "New username cannot be empty"));
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            Optional<User> userOpt = userService.findUserById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token or user not found"));
            }

            User user = userOpt.get();
            userService.setUsername(user, newUsername.trim());

            return ResponseEntity.ok(Map.of(
                "message", "Username changed successfully",
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "email", user.getEmail() != null ? user.getEmail() : ""
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "An error occurred while changing username",
                "details", e.getMessage()
            ));
        }
    }

    @GetMapping("/userSearch/{query}")
    public List<String> userSearch(@PathVariable String query) {
        // Hey ! This function was written by Gabe Gallagher for his really cool search functionality
        // Dec 3, 2025
        //
        try{
            List<String> results = new java.util.ArrayList<>();
            List<User> userResults = userService.findSimilarUsersByUsername(query);
            if(userResults.isEmpty()){
                return results;
            }
            for(int i = 0; i < userResults.size(); i++) {
                results.add(userResults.get(i).getUsername());
            }
            return results;
        }
        catch(Exception e){
            return new java.util.ArrayList<>();
            // returns empty list in case of error
        }
    }

    @GetMapping("/getUserByUsername/{username}")
    public List<String> getUserByUsername(@PathVariable String username) {
        // Gabe Gallagher - LDM Dec 4 2025
        List<String> userInfo = new java.util.ArrayList<>();
        try {
            Optional<User> userOpt = userService.findUserByUsername(username);
            if (userOpt.isEmpty()) {
                userInfo.add("findUserByUsernameError");
                return userInfo;
            }
            userInfo = List.of(
                userOpt.get().getUserId().toString(),
                userOpt.get().getUsername(),
                userOpt.get().getGithubUsername() != null ? userOpt.get().getGithubUsername() : "This User Hasn't Linked A GitHub Account",
                userOpt.get().getEmail() != null ? userOpt.get().getEmail() : "This User Hasn't Added An Email"
            );
            return userInfo;
        } catch (Exception e) {
            userInfo.add("findUserByUsernameError");
            return userInfo;
        }
    }
}