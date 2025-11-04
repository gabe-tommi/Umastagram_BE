/*
 * Author: Armando Vega
 * Created: 03 November 2025
 * Date Last Modified: 03 November 2025
 * Last Modified By: Armando Vega
 * Summary: User Service Unit Test
 */
package com.c11.umastagram.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.c11.umastagram.model.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    
    @Autowired
    private UserService userService;

    @Test
    public void testSaveUser(){
        User user = new User(null, null, "testuser", "test@example.com", "password123");
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser.getUserId());

        User fetchedUser = userService.findUserById(savedUser.getUserId()).orElse(null);
        assertNotNull(fetchedUser);
        assertEquals("testuser", fetchedUser.getUsername());

        try {
            userService.saveUser(fetchedUser);
        } catch (IllegalArgumentException e) {
            assertEquals("User ID already exists", e.getMessage());
        }
        fetchedUser.setUserId(null); // Reset ID to test email duplication
        try {
            userService.saveUser(fetchedUser);
        } catch (IllegalArgumentException e) {
            assertEquals("Email already exists", e.getMessage());
        }
        fetchedUser.setEmail("newemail@example.com"); // Reset email to test password duplication
        fetchedUser.setPassword("");
        try {
            userService.saveUser(fetchedUser);
        } catch (IllegalArgumentException e) {
            assertEquals("Password cannot be null or empty", e.getMessage());
        }
        fetchedUser.setPassword("newpassword123"); // Set valid password
        fetchedUser.setEmail(null);
        try {
            userService.saveUser(fetchedUser);
        } catch (IllegalArgumentException e) {
            assertEquals("Email cannot be null or empty", e.getMessage());
        }
        fetchedUser.setEmail("newemail@example.com");
        fetchedUser.setUsername(null);
        try {
            userService.saveUser(fetchedUser);
        } catch (IllegalArgumentException e) {
            assertEquals("Username cannot be null or empty", e.getMessage());
        }
        fetchedUser.setUsername("newusername");
        fetchedUser.setPassword(null);
        try {
            userService.saveUser(fetchedUser);
        } catch (IllegalArgumentException e) {
            assertEquals("Password cannot be null or empty", e.getMessage());
        }
        fetchedUser.setPassword("newpassword123");
        User newUser = userService.saveUser(fetchedUser);
        assertNotNull(newUser.getUserId());
        assertEquals("newusername", newUser.getUsername());
    }

    @Test
    public void testSaveGitHubUser(){
        enum UserPath{
            EMAIL_NOT_NULL,
            USERNAME_NOT_NULL,
            PASSWORD_NOT_NULL,
            EMAIL_NOT_EMPTY,
            
        }

        User user = new User("1010", "githubUser1010", "githubuser", "githubuser@example.com", "password123");
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser.getUserId());
        assertEquals("githubUser1010", savedUser.getGithubUsername());


    }
    
}