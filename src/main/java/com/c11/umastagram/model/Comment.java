/*
Author: Joceline Cortez-Arellano
Date Last Modified: 4 November 2025
Summary: Comment Model POJO Class
*/

package com.c11.umastagram.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;   // Primary Key

    @Column(name = "user_id", nullable = false)
    private int userId;      // Foreign Key to User 

    @Column(name = "post_id", nullable = false)
    private int postId;      // Foreign Key to Post

    @Column(name = "comment_text", nullable = false, length = 500)
    private String commentText;  // Actual comment content

    // --- Getters and Setters ---
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}