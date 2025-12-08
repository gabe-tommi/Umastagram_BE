/*
Author: Alexangelo Orozco Gutierrez
Date: December 4, 2025
Summary: DTO for Uma list response containing id, name, and image_link
*/

package com.c11.umastagram.dto;

public class UmaListResponse {
    private Long id;
    private String name;
    private String imagePath;

    public UmaListResponse(Long id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
