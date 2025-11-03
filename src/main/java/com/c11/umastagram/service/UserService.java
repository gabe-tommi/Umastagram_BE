package com.c11.umastagram.service;

import com.c11.umastagram.model.User;
import com.c11.umastagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User saveUser(User user) {
        // check for invalid user field entries (MUST HAVE EMAIL, USERNAME, PASSWORD)
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if(user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Add validation, e.g., check if email is unique
        if( userRepository.getUserByUserId(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if( (user.getGithubId() != null && !user.getGithubId().isEmpty()) && userRepository.getUserByGitHubId(user.getGithubId()).isPresent()) {
            throw new IllegalArgumentException("GitHub ID already exists");
        }
        if( user.getGithubUsername() != null && !user.getGithubUsername().isEmpty() && userRepository.getUserByGitHubUsername(user.getGithubUsername()).isPresent()) {
            throw new IllegalArgumentException("GitHub Username already exists");
        }

        // Hash password if needed (use BCrypt)
        
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.getUserByUserId(id);
    }

    public Optional<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUserByUserId(id);
    }

    
}