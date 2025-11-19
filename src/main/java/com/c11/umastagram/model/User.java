/*
 * Author: Armando Vega
 * Created: 28 October 2025
 * Date Last Modified: 28 October 2025
 * Last Modified By: Armando Vega
 * Summary: User Model POJO Class
 */
package com.c11.umastagram.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "uma_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = true, length = 255)
    private String provider;

    @Column(unique = true, nullable = true, length = 255)
    private String googleId;

    @Column(nullable = true, length = 255)
    private String googleUsername;

    @Column(unique = true, nullable = true, length = 255)
    private String githubId;

    @Column(unique = true, nullable = true, length = 255)
    private String githubUsername;

    @Column(nullable = true)
    private LocalDateTime lastLogin;

    @Column(unique = true, nullable = false, length = 255)
    private String username;

    @Column(unique = true, nullable = true, length = 255)
    private String email;

    @Column(nullable = true, length = 255)
    private String password;

    public User(){
        this.username = "";
        this.email = "";
        this.password = "";
    }

    public User(String provider, String providerId, String providerUsername, String username, String email, String password) {
        this.provider = provider;
        this.username = username;
        this.email = email;
        this.password = password;
        
        // Set provider-specific fields
        if ("github".equals(provider)) {
            this.githubId = providerId;
            this.githubUsername = providerUsername;
        } else if ("google".equals(provider)) {
            this.googleId = providerId;
            this.googleUsername = providerUsername;
        }
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleUsername() {
        return googleUsername;
    }

    public void setGoogleUsername(String googleUsername) {
        this.googleUsername = googleUsername;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", provider='" + provider + '\'' +
                ", googleId='" + googleId + '\'' +
                ", googleUsername='" + googleUsername + '\'' +
                ", githubId='" + githubId + '\'' +
                ", githubUsername='" + githubUsername + '\'' +
                ", lastLogin=" + lastLogin +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
