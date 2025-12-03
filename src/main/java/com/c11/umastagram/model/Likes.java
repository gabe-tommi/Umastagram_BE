/*
Author: Alexangelo Orozco Gutierrez
Date: December 2, 2025
Summary: Links the UserId and PostId to represent a like on a post. 
Makes it easy to get what posts a user has liked.
*/

package com.c11.umastagram.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    public Likes() {
        this.dateLiked = LocalDateTime.now();
    }

    public Likes(User user, Posts post) {
        this.user = user;
        this.post = post;
        this.dateLiked = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

}
