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
        String email = user.getEmail() == null ? null : user.getEmail().trim();
        String username = user.getUsername() == null ? null : user.getUsername().trim();
        String password = user.getPassword() == null ? null : user.getPassword().trim();
        String githubId = user.getGithubId() == null ? null : user.getGithubId().trim();
        String githubUsername = user.getGithubUsername() == null ? null : user.getGithubUsername().trim();

        // persist trimmed values back to user
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGithubId(githubId);
        user.setGithubUsername(githubUsername);

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Add validation, e.g., check if email is unique
        if (user.getUserId() != null && userRepository.getUserByUserId(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }
        if (userRepository.getUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (githubId != null && !githubId.isEmpty() && userRepository.getUserByGitHubId(githubId).isPresent()) {
            throw new IllegalArgumentException("GitHub ID already exists");
        }
        if (githubUsername != null && !githubUsername.isEmpty() && userRepository.getUserByGitHubUsername(githubUsername).isPresent()) {
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