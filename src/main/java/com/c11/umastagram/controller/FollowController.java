package com.c11.umastagram.controller;

import com.c11.umastagram.model.Follow;
import com.c11.umastagram.model.FriendRequest;
import com.c11.umastagram.service.FollowService;
import com.c11.umastagram.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/sendFriendRequest/{userId}/{friendId}")
    public void sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        LocalDateTime now = LocalDateTime.now();
        FriendRequest fr = new FriendRequest(userId, friendId, now);
        friendRequestService.saveFriendRequest(fr);
    }

    @GetMapping("/acceptFriendRequest/{userId}/{friendId}")
    public Follow acceptFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        LocalDateTime now = LocalDateTime.now();
        Follow follow = new Follow(userId, friendId, now);

        friendRequestService.deleteFriendRequest(userId, friendId);
        return followService.saveFollow(follow);
    }
}
