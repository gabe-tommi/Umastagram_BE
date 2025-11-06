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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.c11.umastagram.model.User;
import com.c11.umastagram.repository.UserRepository;

import java.lang.StackWalker.Option;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

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
        enum UserPath{ // im so smart
            EMAIL_NOT_NULL,
            USERNAME_NOT_NULL,
            PASSWORD_NOT_NULL,
            EMAIL_NOT_EMPTY,
            USERNAME_NOT_EMPTY,
            PASSWORD_NOT_EMPTY,
            EMAIL_EXISTS,
            GITHUB_USER_ID_EXISTS,
            GITHUB_USERNAME_EXISTS
        } // enum to manage test paths for checks in saving a user with GitHub info

        User user = new User("1010", "githubUser1010", "githubuser", "githubuser@example.com", "password123");
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser.getUserId());
        assertEquals("githubUser1010", savedUser.getGithubUsername());

        User compUser = new User("1010", "anotherGitHubUser", "compuser", "compuser@example.com", "password123");
        for(UserPath path : UserPath.values()){
            try {
                switch(path){
                    case EMAIL_NOT_NULL:
                        compUser.setEmail(null);
                        break;
                    case USERNAME_NOT_NULL:
                        compUser.setUsername(null);
                        break;
                    case PASSWORD_NOT_NULL:
                        compUser.setPassword(null);
                        break;
                    case EMAIL_NOT_EMPTY:
                        compUser.setEmail("");
                        break;
                    case USERNAME_NOT_EMPTY:
                        compUser.setUsername("");
                        break;
                    case PASSWORD_NOT_EMPTY:
                        compUser.setPassword("");
                        break;
                    case GITHUB_USER_ID_EXISTS:
                        compUser.setGithubId("1010");
                        compUser.setGithubUsername("newGitHubUser");
                        compUser.setEmail("newemail@example.com");
                        break;
                    case GITHUB_USERNAME_EXISTS:
                        compUser.setGithubId("2020");
                        compUser.setGithubUsername("githubUser1010");
                        compUser.setEmail("anotheremail@example.com");
                        break;
                    case EMAIL_EXISTS:
                        compUser.setEmail("githubuser@example.com");
                        break;
                    default:
                        break;
                }
                userService.saveUser(compUser);
                throw new IllegalArgumentException("Expected IllegalArgumentException for path: " + path);
            } catch (IllegalArgumentException e) {
                switch(path){
                    case EMAIL_NOT_NULL:
                        assertEquals("Email cannot be null or empty", e.getMessage());
                        compUser.setEmail("compuser@example.com");
                        break;
                    case USERNAME_NOT_NULL:
                        assertEquals("Username cannot be null or empty", e.getMessage());
                        compUser.setUsername("compuser");
                        break;
                    case PASSWORD_NOT_NULL:
                        assertEquals("Password cannot be null or empty", e.getMessage());
                        compUser.setPassword("password123");
                        break;
                    case EMAIL_NOT_EMPTY:
                        assertEquals("Email cannot be null or empty", e.getMessage());
                        compUser.setEmail("compuser@example.com");
                        break;
                    case USERNAME_NOT_EMPTY:
                        assertEquals("Username cannot be null or empty", e.getMessage());
                        compUser.setUsername("compuser");
                        break;
                    case PASSWORD_NOT_EMPTY:
                        assertEquals("Password cannot be null or empty", e.getMessage());
                        compUser.setPassword("password123");
                        break;
                    case GITHUB_USER_ID_EXISTS:
                        assertEquals("GitHub ID already exists", e.getMessage());
                        break;
                    case GITHUB_USERNAME_EXISTS:
                        assertEquals("GitHub Username already exists", e.getMessage());
                        break;
                    case EMAIL_EXISTS:
                        assertEquals("Email already exists", e.getMessage());
                        compUser.setEmail("compuser@example.com");
                        break;
                    default:
                        break;
                }
            }
        }
        compUser.setGithubId("2020");
        compUser.setGithubUsername("newGitHubUser2020");
        compUser.setEmail("newemail2020@example.com");
        User newUser = userService.saveUser(compUser);
        assertNotNull(newUser.getUserId());
        assertEquals("newGitHubUser2020", newUser.getGithubUsername());
        assertEquals(2, userService.getAllUsers().get().size());
    }

    @Test
    public void testGetAllUsers(){
        User user1 = new User("userOne", "userone@gmail.com", "password123");
        User user2 = new User("userTwo", "usertwo@gmail.com", "password456");
        User user3 = new User("userThree", "userthree@gmail.com", "password789");
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        List<User> users = userService.getAllUsers().get();
        assertEquals(3, users.size());
        assertEquals("userOne", users.get(0).getUsername());
        assertEquals("userTwo", users.get(1).getUsername());
        assertEquals("userThree", users.get(2).getUsername());
    }

    @Test
    public void testFindUserById(){
        User user = new User("idUser", "idUser@example.com", "password123");
        userService.saveUser(user);

        User foundUser = userService.findUserById(user.getUserId()).get();
        assertNotNull(foundUser);
        assertEquals("idUser", foundUser.getUsername());
    }

    @Test
    public void testFindUserByEmail(){
        User user = new User("emailUser", "emailUser@example.com", "password123");
        userService.saveUser(user);

        User foundUser = userService.findUserByEmail("emailUser@example.com").get();
        assertNotNull(foundUser);
        assertEquals("emailUser", foundUser.getUsername());
    }

    @Test
    public void testDeleteUser(){
        User user = new User("deleteUser", "deleteUser@example.com", "password123");
        userService.saveUser(user);
        assertNotNull(userService.findUserById(user.getUserId()).get());

        String result = userService.deleteUser(user);
        assertFalse(userService.findUserById(user.getUserId()).isPresent());
        assertEquals("User deleteUser deleted successfully", result);

        User randomUser = new User("randomUser", "randomUser@example.com", "password123");
        String failResult = userService.deleteUser(randomUser);
        assertEquals("User not found or could not be deleted", failResult);
    }

    @Test
    public void testFindUserByUsername(){
        User user = new User("usernameUser", "usernameUser@example.com", "password123");

        userService.saveUser(user);
        User foundUser = userService.findUserByUsername("usernameUser").get();
        assertNotNull(foundUser);
        assertEquals("usernameUser", foundUser.getUsername());
    }
}