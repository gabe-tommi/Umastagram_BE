/*
Author: Alexangelo Orozco Gutierrez
Date: October 29, 2025
Summary: Helps create a post object to then add to the database
*/


package com.c11.umastagram.model;

public class CreatePostRequest {
    private Long userId;
    private String text;
    private String image;

    public CreatePostRequest() {}

    public CreatePostRequest(Long userId, String text, String image) {
        this.userId = userId;
        this.text = text;
        this.image = image;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
