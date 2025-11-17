/*
 * Author: Armando Vega
 * Created: 29 October 2025
 * Date Last Modified: 11 November 2025
 * Last Modified By: Armando Vega
 * Summary: User Repository Unit Test
 */
package com.c11.umastagram.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.c11.umastagram.model.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testInsertUser() {
        User user = new User("github", "01110", "githubUser", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getUserId());

        User user2 = new User(null, null, "testuser2", "test2@example.com", "password123");
        User savedUser2 = userRepository.save(user2);
        assertNotNull(savedUser2.getUserId());
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

    @Test
    public void testGetUserByEmail() {
        User user = new User(null, null, "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        String email = savedUser.getEmail();

        Optional<User> userOpt = userRepository.getUserByEmail(email);
        assertFalse(userOpt.isEmpty());
        assertEquals("testuser", userOpt.get().getUsername());
        assertEquals("test@example.com", userOpt.get().getEmail());
    }

    @Test
    public void testDeleteUserByUserId() {
        User user = new User(null, null, "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        userRepository.deleteUserByUserId(userId);
        Optional<User> userOpt = userRepository.getUserByUserId(userId);
        assertTrue(userOpt.isEmpty());
    }

    @Test
    public void testGetUserByGitHubId() {
        User user = new User("githubId123", "githubUser", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        String githubId = savedUser.getGithubId();

        Optional<User> userOpt = userRepository.getUserByGitHubId(githubId);
        assertFalse(userOpt.isEmpty());
        assertEquals("testuser", userOpt.get().getUsername());
        assertEquals("githubId123", userOpt.get().getGithubId());
    }

    @Test
    public void testGetUserByGitHubUsername() {
        User user = new User("githubId", "githubUser123", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        String githubUsername = savedUser.getGithubUsername();

        Optional<User> userOpt = userRepository.getUserByGitHubUsername(githubUsername);
        assertFalse(userOpt.isEmpty());
        assertEquals("testuser", userOpt.get().getUsername());
        assertEquals("githubUser123", userOpt.get().getGithubUsername());
    }
    
    @Test
    public void testSetGitHubIdByUserId() {
        User user = new User(null, "oldGithubUser", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        userRepository.setGitHubIdByUserId("newGithubId123", userId);
        userRepository.flush();

        Optional<String> githubIdOpt = userRepository.getGitHubIdByUserId(userId.toString());
        assertFalse(githubIdOpt.isEmpty());
        assertEquals("newGithubId123", githubIdOpt.get());
    }

    @Test
    public void testSetGitHubUsernameByUserId() {
        User user = new User("githubId", "oldGithubUsername", "testuser", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        userRepository.setGitHubUsernameByUserId("newGithubUserName456", userId);
        userRepository.flush();

        Optional<String> githubUsernameOpt = userRepository.getGitHubUsernameByUserId(userId);
        assertFalse(githubUsernameOpt.isEmpty());
        assertEquals("newGithubUserName456", githubUsernameOpt.get());
    }

    @Test
    public void testSetUsernameByUserId() {
        User user = new User(null, null, "oldUsername", "test@example.com", "password123");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        userRepository.setUsernameByUserId("newUsername789", userId);
        userRepository.flush();

        Optional<String> usernameOpt = userRepository.getUsernameByUserId(userId);
        assertFalse(usernameOpt.isEmpty());
        assertEquals("newUsername789", usernameOpt.get());
    }

    @Test
    public void testSetPasswordByUserId() {
        User user = new User(null, null, "testuser", "test@example.com", "oldPassword");
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getUserId();

        userRepository.setPasswordByUserId("newSecurePassword!", userId);
        userRepository.flush();

        Optional<String> passwordOpt = userRepository.getPasswordByUserId(userId);
        assertFalse(passwordOpt.isEmpty());
        assertEquals("newSecurePassword!", passwordOpt.get());
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User(null, null, "uniqueUsername", "unique@example.com", "password123");
        User savedUser = userRepository.save(user);
        String username = savedUser.getUsername();

        Optional<User> userOpt = userRepository.getUserByUsername(username);
        assertFalse(userOpt.isEmpty());
        assertNotNull(userOpt);
        assertEquals("uniqueUsername", userOpt.get().getUsername());
        assertEquals("unique@example.com", userOpt.get().getEmail());
    }
}