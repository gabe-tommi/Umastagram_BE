/*
 * Author: Armando Vega
 * Created: 29 October 2025
 * Date Last Modified: 29 October 2025
 * Last Modified By: Armando Vega
 * Summary: User Repository Unit Test
 */
package com.c11.umastagram.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.c11.umastagram.entities.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsertUser() {
        User user = new User("test@example.com", "testuser", "password123", "githubId", "githubUser");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getUserId());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(null, null, "user1", "user1@example.com", "pass1");
        userRepository.save(user1);
        User user2 = new User(null, null, "user2", "user2@example.com", "pass2");
        userRepository.save(user2);
        
        Optional<List<User>> usersOpt = userRepository.getAllUsers();
        assertFalse(usersOpt.isEmpty());
        assertEquals(2, usersOpt.get().size());
    }

    @Test
    public void testGetUserByUserId() {
        User user = new User("githubId", "githubUser", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();
        
        Optional<User> userOpt = userRepository.getUserByUserId(userId);
        assertFalse(userOpt.isEmpty());
        assertEquals("testuser", userOpt.get().getUsername());
    }

    @Test
    public void testGetGitHubIdByUserId() {
        User user = new User("githubId123", "githubUser", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();
        
        Optional<String> githubIdOpt = userRepository.getGitHubIdByUserId(userId.toString());  // Note: param is String
        assertFalse(githubIdOpt.isEmpty());
        assertEquals("githubId123", githubIdOpt.get());
    }

    @Test
    public void testGetGitHubUsernameByUserId() {
        User user = new User("githubId", "githubUser123", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();
        
        Optional<String> githubUsernameOpt = userRepository.getGitHubUsernameByUserId(userId);
        assertFalse(githubUsernameOpt.isEmpty());
        assertEquals("githubUser123", githubUsernameOpt.get());
    }

    @Test
    public void testGetEmailByUserId() {
        User user = new User(null, null, "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();
        
        Optional<String> emailOpt = userRepository.getEmailByUserId(userId);
        assertFalse(emailOpt.isEmpty());
        assertEquals("test@example.com", emailOpt.get());
    }

    @Test
    public void testGetUsernameByUserId() {
        User user = new User(null, null, "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();
        
        Optional<String> usernameOpt = userRepository.getUsernameByUserId(userId);
        assertFalse(usernameOpt.isEmpty());
        assertEquals("testuser", usernameOpt.get());
    }

    @Test
    public void testGetPasswordByUserId() {
        User user = new User(null, null, "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();
        
        Optional<String> passwordOpt = userRepository.getPasswordByUserId(userId);
        assertFalse(passwordOpt.isEmpty());
        assertEquals("password123", passwordOpt.get());
    }
}
