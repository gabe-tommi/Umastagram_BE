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
import java.util.Map;

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
        String email = user.getEmail() == null || user.getEmail().isEmpty() ? null : user.getEmail().trim().toLowerCase();
        String username = user.getUsername() == null|| user.getUsername().isEmpty() ? null : user.getUsername().trim();
        String password = user.getPassword() == null || user.getPassword().isEmpty() ? null : user.getPassword().trim();
        String githubId = user.getGithubId() == null || user.getGithubId().isEmpty() ? null : user.getGithubId().trim();
        String githubUsername = user.getGithubUsername() == null || user.getGithubUsername().isEmpty() ? null : user.getGithubUsername().trim();
        String googleId = user.getGoogleId() == null || user.getGoogleId().isEmpty() ? null : user.getGoogleId().trim();
        String googleUsername = user.getGoogleUsername() == null || user.getGoogleUsername().isEmpty() ? null : user.getGoogleUsername().trim();
        String provider = user.getProvider() == null || user.getProvider().isEmpty() ? null : user.getProvider().trim();

        // persist trimmed values back to user
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGithubId(githubId);
        user.setGithubUsername(githubUsername);
        user.setGoogleId(googleId);
        user.setGoogleUsername(googleUsername);
        user.setProvider(provider);

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        // Allow null password for OAuth users, but require it for non-OAuth users
        if (provider == null && (password == null || password.isEmpty())) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Add validation, e.g., check if email is unique
        if (user.getUserId() != null && userRepository.getUserByUserId(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }
        if (userRepository.getUserByEmail(email) != null && userRepository.getUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.getUserByUsername(username) != null && userRepository.getUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if(provider != null && provider.equals("github")) {
            if (githubId != null && !githubId.isEmpty() && userRepository.getUserByGitHubId(githubId).isPresent()) {
                throw new IllegalArgumentException("GitHub ID already exists");
            }
            if (githubUsername != null && !githubUsername.isEmpty() && userRepository.getUserByGitHubUsername(githubUsername).isPresent()) {
                throw new IllegalArgumentException("GitHub Username already exists");
            }
        } else if(provider != null && provider.equals("google")) {
            if (googleId != null && !googleId.isEmpty() && userRepository.getUserByGoogleId(googleId).isPresent()) {
                throw new IllegalArgumentException("Google ID already exists");
            }
            if (googleUsername != null && !googleUsername.isEmpty() && userRepository.getUserByGoogleUsername(googleUsername).isPresent()) {
                throw new IllegalArgumentException("Google Username already exists");
            }
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
     * Finds a user by their username.
     * @param username The username of the user
     * @return An Optional containing the user if found, or empty if not
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.getUserByUsername(username.trim())
                .filter(user -> user != null && !user.getUsername().isEmpty());
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
     * Creates or updates an OAuth user. For OAuth users, password is not required.
     * If user exists by provider + providerId, updates their info. Otherwise creates new user.
     * @param provider The OAuth provider (github/google)
     * @param userInfo Map containing user info from OAuth provider
     * @return The created or updated user
     */
    public User createOrUpdateOAuthUser(String provider, Map<String, Object> userInfo) {
        String providerId = (String) userInfo.get("id");
        
        // Try to find existing user by provider + providerId
        Optional<User> existingUser = findByProviderAndProviderId(provider, providerId);
        
        User user;
        if (existingUser.isPresent()) {
            // Update existing user
            user = existingUser.get();
            user.setEmail((String) userInfo.get("email"));
            user.setLastLogin(java.time.LocalDateTime.now());
            // Could update avatar_url if needed
        } else {
            // Create new OAuth user
            user = new User(
                provider,
                providerId,
                (String) userInfo.get("username"), // provider username
                (String) userInfo.get("username"), // app username (same for OAuth)
                (String) userInfo.get("email"),
                null // no password for OAuth users
            );
            user.setLastLogin(java.time.LocalDateTime.now());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Finds a user by provider and provider ID.
     * @param provider The OAuth provider
     * @param providerId The ID from the OAuth provider
     * @return Optional containing the user if found
     */
    private Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        if ("github".equals(provider)) {
            return userRepository.getUserByGitHubId(providerId);
        } else if ("google".equals(provider)) {
            return userRepository.getUserByGoogleId(providerId);
        }
        return Optional.empty();
    }
}