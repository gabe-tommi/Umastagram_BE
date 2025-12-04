/*
 * Author: Armando Vega
 * Created: 29 October 2025
 * Date Last Modified: 17 November 2025
 * Last Modified By: Armando Vega
 * Summary: User Model POJO Class Unit Test
 */
package com.c11.umastagram.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;


public class UserTest {
    @Test
    public void testUserCreation() {
        User user = new User();
        // user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testUserDefaultValues() {
        User user = new User();

        assertEquals("", user.getUsername());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
    }

    @Test
    public void testUserWithGitHubInfo() {
        User user = new User("github", "githubId123", "githubUser", "testuser", "test@gmail.com", "pass123");
        
        assertEquals("githubId123", user.getGithubId());
        assertEquals("githubUser", user.getGithubUsername());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("pass123", user.getPassword());
    }

    @Test
    public void testUserWithGoogleInfo() {
        User user2 = new User("google", "googleId123", "googleUser", "testuser2", "test2@example.com", "password456");

        assertEquals("google", user2.getProvider());
        assertEquals("googleId123", user2.getGoogleId());
        assertEquals("googleUser", user2.getGoogleUsername());
        assertEquals("testuser2", user2.getUsername());
        assertEquals("test2@example.com", user2.getEmail());
        assertEquals("password456", user2.getPassword());
    }

    @Test
    public void testSetAndGetUserId() {
        User user = new User();
        Long expected = 1L;
        user.setUserId(expected);
        assertEquals(expected, user.getUserId());
    }

    @Test
    public void testSetAndGetGithubId() {
        User user = new User();
        String expected = "githubId123";
        user.setGithubId(expected);
        assertEquals(expected, user.getGithubId());
    }

    @Test
    public void testSetAndGetGithubUsername() {
        User user = new User();
        String expected = "githubUser";
        user.setGithubUsername(expected);
        assertEquals(expected, user.getGithubUsername());
    }

    @Test
    public void testSetAndGetGoogleId() {
        User user = new User();
        String expected = "googleId123";
        user.setGoogleId(expected);
        assertEquals(expected, user.getGoogleId());
    }

    @Test
    public void testSetAndGetGoogleUsername() {
        User user = new User();
        String expected = "googleUser";
        user.setGoogleUsername(expected);
        assertEquals(expected, user.getGoogleUsername());
    }

    @Test
    public void testSetAndGetProvider() {
        User user = new User();
        String expected = "google";
        user.setProvider(expected);
        assertEquals(expected, user.getProvider());
    }

    @Test
    public void testSetAndGetLastLogin() {
        User user = new User();
        LocalDateTime lastLogin = LocalDateTime.now();
        user.setLastLogin(lastLogin);
        assertEquals(lastLogin, user.getLastLogin());
    }

    @Test
    public void testSetAndGetUsername() {
        User user = new User();
        String expected = "testuser";
        user.setUsername(expected);
        assertEquals(expected, user.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        User user = new User();
        String expected = "test@example.com";
        user.setEmail(expected);
        assertEquals(expected, user.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        User user = new User();
        String expected = "password123";
        user.setPassword(expected);
        assertEquals(expected, user.getPassword());
    }
}
