/*
Author: Alexangelo Orozco Gutierrez
Date: December 2, 2025
Summary: Contains the API functions for the Likes entity
*/

package com.c11.umastagram.controller;

import com.c11.umastagram.model.Likes;
import com.c11.umastagram.model.Posts;
import com.c11.umastagram.model.User;
import com.c11.umastagram.repository.LikesRepository;
import com.c11.umastagram.repository.PostsRepository;
import com.c11.umastagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikesController {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a like to a post
     * POST /api/likes
     */
    @PostMapping
    public ResponseEntity<?> addLike(@RequestParam Long userId, @RequestParam Long postId) {
        try {
            // Check if user exists
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Check if post exists
            Posts post = postsRepository.findById(postId).orElse(null);
            if (post == null) {
                return ResponseEntity.badRequest().body("Post not found");
            }

            // Check if user already liked this post
            if (likesRepository.existsByUserIdAndPostId(userId, postId)) {
                return ResponseEntity.badRequest().body("User already liked this post");
            }

            // Create and save like
            Likes like = new Likes(user, post);
            Likes savedLike = likesRepository.save(like);

            // Increment post's like count
            post.incrementLikes();
            postsRepository.save(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error adding like: " + e.getMessage());
        }
    }

    /**
     * Remove a like from a post
     * DELETE /api/likes/user/{userId}/post/{postId}
     */
    @DeleteMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<?> removeLike(@PathVariable Long userId, @PathVariable Long postId) {
        try {
            Likes like = likesRepository.findByUserIdAndPostId(userId, postId);
            if (like == null) {
                return ResponseEntity.notFound().build();
            }

            likesRepository.delete(like);

            // Decrement post's like count
            Posts post = postsRepository.findById(postId).orElse(null);
            if (post != null) {
                post.decrementLikes();
                postsRepository.save(post);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all likes for a post
     * All userIDs who liked the post will be retrievable
     * GET /api/likes/post/{postId}
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Likes>> getLikesForPost(@PathVariable Long postId) {
        try {
            List<Likes> likes = likesRepository.findByPostId(postId);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all likes by a user
     * This means it gets all entries where the userID matches
     * This will also give all postsIDs a user liked
     * GET /api/likes/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Likes>> getLikesByUser(@PathVariable Long userId) {
        try {
            List<Likes> likes = likesRepository.findByUserId(userId);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
