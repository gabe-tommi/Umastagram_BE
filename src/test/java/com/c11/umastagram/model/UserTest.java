/*
 * Author: Armando Vega
 * Created: 29 October 2025
 * Date Last Modified: 29 October 2025
 * Last Modified By: Armando Vega
 * Summary: User Model POJO Class Unit Test
 */
package com.c11.umastagram.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import com.c11.umastagram.entities.User;


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
        User user = new User("githubId123", "githubUser", "testuser", "test@gmail.com", "pass123");
        
        assertEquals("githubId123", user.getGithubId());
        assertEquals("githubUser", user.getGithubUsername());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("pass123", user.getPassword());
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
