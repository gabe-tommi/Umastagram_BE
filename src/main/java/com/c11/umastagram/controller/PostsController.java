/*
Author: Alexangelo Orozco Gutierrez
Date: October 29, 2025
Summary: Contains the API functions for the Posts entity
*/


package com.c11.umastagram.controller;

import com.c11.umastagram.entities.Posts;
import com.c11.umastagram.repository.PostsRepository;
import com.c11.umastagram.entities.CreatePostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostsRepository postsRepository;

    /**
     * Create a new post
     * POST /api/posts
     */
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request) {
        try {
            // Validate required fields
            if (request.getUserId() == null || request.getUserId().isEmpty()) {
                return ResponseEntity.badRequest().body("userId is required");
            }
            if (request.getText() == null || request.getText().isEmpty()) {
                return ResponseEntity.badRequest().body("text is required");
            }

            // Create new post with provided data
            Posts newPost = new Posts(
                request.getUserId(),
                request.getText(),
                request.getImage(),
                LocalDateTime.now()  // Automatically set current date/time
            );

            // Save to database
            Posts savedPost = postsRepository.save(newPost);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating post: " + e.getMessage());
        }
    }

    /**
     * Get all posts
     * GET /api/posts
     */
    @GetMapping
    public ResponseEntity<List<Posts>> getAllPosts() {
        try {
            List<Posts> posts = postsRepository.findAll();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get posts by a specific user
     * GET /api/posts/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Posts>> getPostsByUser(@PathVariable String userId) {
        try {
            List<Posts> posts = postsRepository.findByUserId(userId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a post by ID
     * GET /api/posts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            return postsRepository.findById(id)
                .map(post -> ResponseEntity.ok((Object) post))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a post
     * DELETE /api/posts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            if (postsRepository.existsById(id)) {
                postsRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
