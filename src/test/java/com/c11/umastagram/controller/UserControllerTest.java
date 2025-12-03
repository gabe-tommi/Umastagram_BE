package com.c11.umastagram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.c11.umastagram.service.UserService;
import com.c11.umastagram.util.JwtUtil;
import com.c11.umastagram.dto.SignupRequest;
import com.c11.umastagram.dto.LoginRequest;
import com.c11.umastagram.model.User;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        testUser.setLastLogin(LocalDateTime.now());
    }

    @AfterEach
    public void tearDown() {
        testUser = null;
    }
    
    @Test
    public void testSignup_Success() throws Exception {
        // Mock UserService to return saved user
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        // Create SignupRequest DTO
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("testuser@example.com");
        signupRequest.setPassword("password123");

        String requestJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(testUser.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testSignup_MissingEmail() throws Exception {
        // Mock service to throw exception for missing email
        when(userService.saveUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("Email cannot be null or empty"));

        // Create SignupRequest DTO with missing email
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setPassword("password123");

        String requestJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email cannot be null or empty"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        // Mock service to return user
        when(userService.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("mocked-jwt-token");

        // Create LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.message").value("Login successful for user: testuser"));
    }

    @Test
    public void testLogin_InvalidUsername() throws Exception {
        // Mock service to return empty for non-existent user
        when(userService.findUserByUsername("wronguser")).thenReturn(Optional.empty());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wronguser");
        loginRequest.setPassword("password123");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("An error occurred during login"));
    }

    @Test
    public void testLogin_InvalidPassword() throws Exception {
        // Mock service to return user
        when(userService.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    public void testLogin_MissingCredentials() throws Exception {
        when(userService.findUserByUsername(anyString())).thenReturn(Optional.empty());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username and password are required"));
    }
}
