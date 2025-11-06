/*
 * Author: Armando Vega
 * Created: 03 November 2025
 * Date Last Modified: 03 November 2025
 * Last Modified By: Armando Vega
 * Summary: User Service Class; Business Logic for User Entity
 */
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

    /**
     * Saves a user to the repository after validating the input. Ensures
     * that required fields are present and unique constraints are met.
     * Trims whitespace from string fields and converts email to lowercase.
     * @param user The user to be saved
     * @return The saved user
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    public User saveUser(User user) {
        
        // check for invalid user field entries (MUST HAVE EMAIL, USERNAME, PASSWORD)
        String email = user.getEmail() == null ? null : user.getEmail().trim().toLowerCase();
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

    /**
     * Finds a user by their ID.
     * @param id The userId of the user
     * @return An Optional containing the user if found, or empty if not
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.getUserByUserId(id);
    }

    /**
     * Retrieves all users.
     * @return An Optional containing a list of all users, or Optional.empty() if none found
     */
    public Optional<List<User>> getAllUsers() {
        return userRepository.getAllUsers()
                .filter(users -> !users.isEmpty() && users != null);
    }

    /**
     * Finds a user by their email.
     * @param email The email of the user
     * @return An Optional containing the user if found, or empty if not
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.getUserByEmail(email.trim().toLowerCase())
                .filter(user -> user != null && !user.getEmail().isEmpty());
    }

    /**
     * Deletes a user.
     * @param user The user to delete
     */
    public String deleteUser(User user) {
        Optional<User> userOpt = userRepository.getUserByUserId(user.getUserId());
        if (userOpt.isPresent() && userOpt.get().getUserId() != null) {
            String username = userOpt.get().getUsername();
            userRepository.deleteUserByUserId(user.getUserId());
            return "User " + username + " deleted successfully";
        }
        return "User not found or could not be deleted";
    }

    /**
     * Finds a user by their username.
     * @param username The username of the user
     * @return An Optional containing the user if found, or empty if not
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.getUserByUsername(username.trim())
                .filter(user -> user != null && !user.getUsername().isEmpty());
    }

    
}