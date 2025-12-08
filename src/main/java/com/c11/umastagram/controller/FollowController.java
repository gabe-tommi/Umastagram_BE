package com.c11.umastagram.controller;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.service.FollowService;
import com.c11.umastagram.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FollowController {

    private final FriendRequestService friendRequestService;
    private final FollowService followService;

    @Autowired
    public FollowController(FriendRequestService friendRequestService, FollowService followService) {
        this.friendRequestService = friendRequestService;
        this.followService = followService;
    }

    @GetMapping("/getUserFollowers/{userId}")
    public List<String> getUserFollowers(@PathVariable Long userId) {
        return followService.getUserFollowers(userId);
    }

    @GetMapping("/getUserFriendRequests/{userId}")
    public List<String> getUserFriendRequests(@PathVariable Long userId) {
        return friendRequestService.getUserFriendRequests(userId);
    }

    @PostMapping("/sendFriendRequest/{userId}/{friendId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            FriendRequest fr = new FriendRequest(userId, friendId, now);
            friendRequestService.saveFriendRequest(fr);
            return ResponseEntity.ok("Friend request sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/acceptFriendRequest/{userId}/{friendId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
//        LocalDateTime now = LocalDateTime.now();
//        Follow follow = new Follow(userId, friendId, now);
//
//        friendRequestService.deleteFriendRequest(userId, friendId);
//        return followService.saveFollow(follow);

        try {
            LocalDateTime now = LocalDateTime.now();
            Follow follow = new Follow(userId, friendId, now);
            followService.saveFollow(follow);
            friendRequestService.deleteFriendRequest(userId, friendId);
            return ResponseEntity.ok("Friend request accepted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
