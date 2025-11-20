/*
    Author: Armando Vega
    Date Created: 19 November 2025
    Description: DTO for user signup requests
*/
package com.c11.umastagram.dto;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
