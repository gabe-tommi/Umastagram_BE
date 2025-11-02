/*
Author: Alexangelo Orozco Gutierrez
Date: October 29, 2025
Summary: Helps create a post object to then add to the database
*/


package com.c11.umastagram.entities;

public class CreatePostRequest {
    private String userId;
    private String text;
    private String image;

    public CreatePostRequest() {}

    public CreatePostRequest(String userId, String text, String image) {
        this.userId = userId;
        this.text = text;
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
